package com.labify.backend.notification.controller;

import com.labify.backend.auth.service.CustomUserDetails;
import com.labify.backend.notification.dto.NotificationResponseDto;
import com.labify.backend.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotifiationController {

    private final NotificationService notificationService;

    // [GET] /notifications
    // [GET] /notifications?isRead=false
    // 사용자별 알림 목록 조회 / 읽지 않은 알림만 조회
    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> getNotifications(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) Boolean isRead) {

        List<NotificationResponseDto> notifications =
                notificationService.getNotificationsByUser(userDetails.getUser(), isRead);
        return ResponseEntity.ok(notifications);
    }
}
