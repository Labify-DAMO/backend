package com.labify.backend.pickup.request.entity;

import com.labify.backend.lab.entity.Lab;
import com.labify.backend.pickup.entity.Pickup;
import com.labify.backend.pickup.request.entity.PickupRequestItem;
import com.labify.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class) // JPA Auditing 활성화
@Getter
@Setter
public class PickupRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pickup_request_id")
    private Long id;

    // 요청을 보낸 실험실
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_id", nullable = false)
    private Lab lab;

    // 요청을 보낸 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    // 희망 수거 날짜
    @Column(nullable = false)
    private LocalDate requestDate;

    // 요청 상태
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PickupRequestStatus status;

    // 요청에 포함된 폐기물 목록 (1:N 관계)
    @OneToMany(mappedBy = "pickupRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PickupRequestItem> items = new ArrayList<>();

    // 이 요청에 대해 배정된 수거 작업 (1:1 관계)
    @OneToOne(mappedBy = "pickupRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private Pickup pickup;

    // 요청 생성 시각
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
}