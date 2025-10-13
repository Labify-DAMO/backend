package com.labify.backend.notification.entity;

import com.labify.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    // 알림 받는 대상자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    // 알림 종류 (연구실 개설 요청, 수거 요청, 수거 완료 등)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NotificationType type;

    // 관련 리소스(요청 ID 등) → 어떤 요청/수거와 연결되는지
    @Column(nullable = false)
    private Long referenceId;

    // 알림 제목 또는 간단한 메시지
    @Column(nullable = false, length = 100)
    private String title;

    // 상세 메시지 (UI에 표시할 본문)
    @Column(columnDefinition = "TEXT")
    private String message;

    // 읽음 여부
    @Column(nullable = false)
    private boolean isRead;

    // 생성 시각
    @CreatedDate
    private LocalDateTime createdAt;
}
