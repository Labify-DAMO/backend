package com.labify.backend.notification.service;

import com.labify.backend.lab.request.entity.LabRequest;
import com.labify.backend.notification.entity.Notification;
import com.labify.backend.notification.entity.NotificationType;
import com.labify.backend.notification.repository.NotificationRepository;
import com.labify.backend.pickup.entity.Pickup;
import com.labify.backend.pickup.request.entity.PickupRequest;
import com.labify.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // 실험실 개설 요청 알림
    public void sendLabRequestNotification(User recipient, LabRequest labRequest) {
        Notification notification = Notification.builder()
                .recipient(recipient)
                .type(NotificationType.LAB_REQUEST)
                .referenceId(labRequest.getRequestId())
                .title("새로운 연구실 개설 요청이 등록되었습니다")
                .message("연구소 '" + labRequest.getName() + "'에서 개설 요청을 보냈습니다.")
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }

    // 수거 요청 발생 시 알림
    public void sendPickupRequestNotification(User recipient, PickupRequest pickupRequest) {
        Notification notification = Notification.builder()
                .recipient(recipient)
                .type(NotificationType.PICKUP_REQUEST)
                .referenceId(pickupRequest.getId())
                .title("새로운 수거 요청이 도착했습니다")
                .message("연구소 '" + pickupRequest.getLab().getName() + "'에서 수거 요청을 보냈습니다.\n" +
                        "희망 수거일: " + pickupRequest.getRequestDate())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }

    // 수거 완료 시 알림
    public void sendPickupCompletedNotification(User recipient, Pickup pickup) {
        Notification notification = Notification.builder()
                .recipient(recipient)
                .type(NotificationType.PICKUP_COMPLETED)
                .referenceId(pickup.getId())
                .title("폐기물 수거가 완료되었습니다")
                .message("요청하신 폐기물 수거가 완료되었습니다.\n처리 일시: "
                        + pickup.getProcessedAt())
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }

}
