package com.labify.backend.pickup.request.dto;

import com.labify.backend.pickup.request.entity.PickupRequest;
import com.labify.backend.pickup.request.entity.PickupRequestStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PickupRequestResponseDto {
    private Long pickupRequestId;
    private Long labId;
    private String labName;
    private Long pickupId;
    private Long collectorId;
    private String collectorName;
    private PickupRequestStatus status;
    private LocalDate requestDate;
    private LocalDateTime createdAt;

    public static PickupRequestResponseDto from(PickupRequest pickupRequest) {
        return PickupRequestResponseDto.builder()
                .pickupRequestId(pickupRequest.getId())
                .labId(pickupRequest.getLab().getLabId())
                .labName(pickupRequest.getLab().getName())
                .pickupId(pickupRequest.getPickup().getId())
                .collectorId(pickupRequest.getPickup().getCollector().getUserId())
                .collectorName(pickupRequest.getPickup().getCollector().getName())
                .status(pickupRequest.getStatus())
                .requestDate(pickupRequest.getRequestDate())
                .createdAt(pickupRequest.getCreatedAt())
                .build();
    }
}
