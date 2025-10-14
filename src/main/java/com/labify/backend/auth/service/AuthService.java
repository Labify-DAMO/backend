package com.labify.backend.auth.service;

import com.labify.backend.auth.dto.LoginRequest;
import com.labify.backend.auth.dto.LoginResponse;
import com.labify.backend.auth.dto.RefreshTokenRequest;
import com.labify.backend.auth.dto.SignupRequest;
import com.labify.backend.auth.entity.EmailVerificationCode;
import com.labify.backend.auth.entity.RefreshToken;
import com.labify.backend.auth.jwt.JwtProvider;
import com.labify.backend.auth.repository.EmailVerificationCodeRepository;
import com.labify.backend.auth.repository.RefreshTokenRepository;
import com.labify.backend.user.entity.Provider;
import com.labify.backend.user.entity.User;
import com.labify.backend.user.entity.UserStatus;
import com.labify.backend.user.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Ref;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final EmailVerificationCodeRepository codeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    // 회원가입(정보 입력)
    public void signup(SignupRequest request) {
        // 이메일 중복 체크
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .affiliation(request.getAffiliation())
                .agreeTerms(request.isAgreeTerms())
                .status(UserStatus.UNVERIFIED)
                .provider(Provider.LOCAL)
                .providerId(null)
                .build();
        userRepository.save(user);
    }

    // 코드 입력 시도 가능 횟수
    private static final int MAX_ATTEMPTS = 5;

    // 코드 검증 로직
    @Transactional
    public boolean verify(String email, String inputCode) {
        EmailVerificationCode latest = codeRepository.findTopByEmailOrderByIdDesc(email).orElse(null);
        if (latest == null) return false;

        if (latest.isUsed() ||
                latest.getExpiresAt().isBefore(LocalDateTime.now()) ||
                latest.getAttemptCount() >= MAX_ATTEMPTS) return false;

        boolean match = latest.getCode().equals(inputCode);
        latest.setAttemptCount(latest.getAttemptCount() + 1);

        if (!match) {
            codeRepository.save(latest);
            return false;
        }
        latest.setUsed(true);
        codeRepository.save(latest);

        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) return false;
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
        return true;
    }

    // 로그인
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        // 이메일 인증되었는지 확인
        if (user.getStatus() == UserStatus.UNVERIFIED) {
            throw new IllegalStateException("이메일 인증이 완료되지 않았습니다. 이메일을 확인해주세요.");
        }

        // accessToken 생성
        String accessToken = jwtProvider.generateAccessToken(user.getEmail());

        // refreshToken 생성
        String refreshToken = jwtProvider.generateRefreshToken(user.getEmail());

        // DB에 저장할 refreshToken의 expiresAt 설정
        LocalDateTime expiresAt = LocalDateTime.now()
                .plus(Duration.ofMillis(jwtProvider.getRefreshTokenExpirationTime()));

        // 생성한 refreshToken을 DB에 저장
        RefreshToken refreshTokenDB = RefreshToken.builder()
                .user(user)
                .tokenHash(jwtProvider.hashToken(refreshToken))
                .deviceInfo("web")            // 지금은 하드코딩함. 나중에 User-Agent에서 가져올 수 있다고 함,,
                .expiresAt(expiresAt)
                .revoked(false)
                .rotatedFrom(null)
                .build();

        refreshTokenRepository.save(refreshTokenDB);

        return new LoginResponse(accessToken, refreshToken);
    }

    // accessToken 재발급
    public LoginResponse refresh(String refreshToken) {
        String email;
        try {
            email = jwtProvider.parseToken(refreshToken).getSubject();
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("Refresh Token이 만료되었습니다.");
        } catch (JwtException e) {
            throw new IllegalArgumentException("Refresh Token이 유효하지 않습니다.");
        }
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        List<RefreshToken> tokens = refreshTokenRepository.findAllByUserAndRevokedFalse(user);

        String hashed = jwtProvider.hashToken(refreshToken);

        RefreshToken refreshTokenDB = tokens.stream()
                .filter(t -> t.getTokenHash().equals(hashed))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Refresh Token이 유효하지 않습니다."));


        if (refreshTokenDB.isRevoked() || refreshTokenDB.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Refresh Token이 만료되었습니다.");
        }

        String newAccessToken = jwtProvider.generateAccessToken(user.getEmail());
        return new LoginResponse(newAccessToken, refreshToken);
    }

    // 로그아웃
    public void logout(String refreshToken) {
        String email = jwtProvider.parseToken(refreshToken).getSubject();
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        List<RefreshToken> tokens = refreshTokenRepository.findAllByUserAndRevokedFalse(user);

        String hashed = jwtProvider.hashToken(refreshToken);

        RefreshToken refreshTokenDB = tokens.stream()
                .filter(t -> t.getTokenHash().equals(hashed))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Refresh Token이 유효하지 않습니다."));

        refreshTokenDB.setRevoked(true);
        refreshTokenRepository.save(refreshTokenDB);

    }

}
