package com.labify.backend.auth.service;

import com.labify.backend.auth.dto.LoginRequest;
import com.labify.backend.auth.dto.SignupRequest;
import com.labify.backend.auth.entity.EmailVerificationCode;
import com.labify.backend.auth.repository.EmailVerificationCodeRepository;
import com.labify.backend.user.entity.Provider;
import com.labify.backend.user.entity.User;
import com.labify.backend.user.entity.UserStatus;
import com.labify.backend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final EmailVerificationCodeRepository codeRepository;
    private final PasswordEncoder passwordEncoder;

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
    public void login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("이메일을 찾을 수 없습니다."));

    }

}
