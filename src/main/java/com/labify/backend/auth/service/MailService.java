package com.labify.backend.auth.service;

import com.labify.backend.auth.entity.EmailVerificationCode;
import com.labify.backend.auth.repository.EmailVerificationCodeRepository;
import com.labify.backend.user.entity.User;
import com.labify.backend.user.entity.UserStatus;
import com.labify.backend.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

// 이 파일 하는 일
// 1. 랜덤 6자리 코드 생성
// 2. 생성 코드를 EmailVerificationCode 테이블에 저장
// 3. 메일 발송

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final EmailVerificationCodeRepository codeRepository;
    private static SecureRandom secureRandom = new SecureRandom();

    // 랜덤 6자리 코드 생성
    public static String createCode() {
        int n = secureRandom.nextInt(900_000) + 100_000; // 100000 ~ 999999
        return String.valueOf(n);
    }

    // 생성 코드를 EmailVerificationCode 테이블에 저장 및 메일 발송
    public void mailSend(String email) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        String code = createCode();
        EmailVerificationCode saved = codeRepository.save(
                EmailVerificationCode.builder()
                        .email(email)
                        .code(code)
                        .expiresAt(LocalDateTime.now().plusMinutes(5))
                        .used(false)
                        .attemptCount(0)
                        .build()
        );

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("Labify 이메일 인증 코드");
        helper.setText("인증코드: " + code + "\n5분 내에 입력해주세요.", true);
        javaMailSender.send(message);
    }

}

