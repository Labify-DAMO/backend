package com.labify.backend.notification.repository;

import com.labify.backend.notification.entity.Notification;
import com.labify.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // 사용자의 모든 알림 조회 (최신순)
    List<Notification> findByRecipientOrderByCreatedAtDesc(User recipient);

    // 사용자의 읽음/안읽음 알림 조회 (최신순)
    List<Notification> findByRecipientAndIsReadOrderByCreatedAtDesc(User recipient, boolean isRead);

    // 읽지 않은 알림 개수
    long countByRecipientAndIsRead(User recipient, boolean isRead);

    // 읽지 않은 알림 목록
    List<Notification> findByRecipientAndIsRead(User recipient, boolean isRead);
}
