package com.labify.backend.notification.controller;

import com.labify.backend.auth.service.CustomUserDetails;
import com.labify.backend.notification.dto.NotificationResponseDto;
import com.labify.backend.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    // [GET] /notifications/unread-count
    // 읽지 않은 알림 개수
    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Long>> getUnreadCount(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        long count = notificationService.getUnreadCount(userDetails.getUser());
        return ResponseEntity.ok(Map.of("unreadCount", count));
    }

    // [PATCH] /notifications/{notificationId}/read
    // 알림 읽음 처리
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponseDto> markAsRead(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        NotificationResponseDto notification =
                notificationService.markAsRead(notificationId, userDetails.getUser());
        return ResponseEntity.ok(notification);
    }
}
