package com.labify.backend.pickup.request.entity;

import com.labify.backend.disposal.entity.DisposalItem;
import com.labify.backend.pickup.request.entity.PickupRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PickupRequestItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pickup_item_id")
    private Long id;

    // 어떤 '수거 요청'에 속해있는지 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pickup_request_id", nullable = false)
    private PickupRequest pickupRequest;

    // 어떤 '폐기물'이 포함되어 있는지 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disposal_id", nullable = false)
    private DisposalItem disposalItem;
}