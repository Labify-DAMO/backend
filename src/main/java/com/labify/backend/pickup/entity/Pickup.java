package com.labify.backend.pickup.entity;

import com.labify.backend.pickup.request.entity.PickupRequest;
import com.labify.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Pickup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // '수거 요청'과의 1:1 관계 추가
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickup_request_id", nullable = false)
    private PickupRequest pickupRequest;

    // 수거 담당자는 나중에 지정될 수 있으므로 nullable = true
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collector_id")
    private User collector;

    @Column(nullable = false)
    private LocalDateTime processedAt; // @CreatedDate 제거, 서비스에서 직접 시간을 설정

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PickupStatus status;

    private String note;
}