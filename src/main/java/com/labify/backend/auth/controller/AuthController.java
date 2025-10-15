package com.labify.backend.auth.controller;

import com.labify.backend.auth.dto.*;
import com.labify.backend.auth.service.AuthService;
import com.labify.backend.auth.service.MailService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")

public class AuthController {
    private final AuthService authService;
    private final MailService mailService;

    // [POST] /auth/signup
    // 회원가입
    // 고민: 지금 프레임 워크 보고 signup api를 누르면 메일 전송을 하게 만들었는데 추후 로직 분리가 필요할 수 있음
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        authService.signup(request);
        try {
            mailService.mailSend(request.getEmail());
            return ResponseEntity.ok("이메일로 인증코드를 보냈습니다.");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("메일 전송에 실패했습니다.");
        }
    }

    // [POST] /auth/send-code
    // 코드 재전송
    @PostMapping("/send-code")
    public ResponseEntity<?> resendCode(@RequestBody EmailRequest request) {
        try {
            mailService.mailSend(request.getEmail());
            return ResponseEntity.ok("인증코드를 재발송했습니다.");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("메일 전송에 실패했습니다.");
        }
    }

    // [POST] /auth/verify-code
    // 코드 검증
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody VerifyCodeRequest request) {
        boolean ok = authService.verify(request.getEmail(), request.getCode());
        return ok ? ResponseEntity.ok("이메일 인증 성공!")
                : ResponseEntity.badRequest().body("유효하지 않거나 만료/시도초과된 코드입니다.");
    }

    // [POST] /auth/login
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    // [POST] /auth/refresh
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshTokenRequest request) {
        LoginResponse response = authService.refresh(request.getRefreshToken());

        return ResponseEntity.ok(response);
    }

    // [POST] /auth/logout
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok("로그아웃 성공");
    }

    // [DELETE] /auth/withdraw
    // 회원 탈퇴 api
    @DeleteMapping("/withdraw")
    public ResponseEntity<Map<String, String>> withdraw(@RequestHeader("Authorization") String authHeader) {
        String refreshToken = authHeader.replace("Bearer ", "").trim();

        authService.withdraw(refreshToken);
        Map<String, String> response = new HashMap<>();
        response.put("message", "회원 탈퇴가 완료되었습니다.");

        return ResponseEntity.ok(response); // 200 OK + JSON 응답
    }

}
