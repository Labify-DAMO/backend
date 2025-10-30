package com.labify.backend.pickup.request.dto;

import com.labify.backend.pickup.request.entity.PickupRequest;
import com.labify.backend.pickup.request.entity.PickupRequestStatus;
import com.labify.backend.user.entity.User;
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
        User collector = pickupRequest.getPickup().getCollector();

        return PickupRequestResponseDto.builder()
                .pickupRequestId(pickupRequest.getId())
                .labId(pickupRequest.getLab().getLabId())
                .labName(pickupRequest.getLab().getName())
                .pickupId(pickupRequest.getPickup().getId())
                .collectorId(collector != null ? collector.getUserId() : null)
                .collectorName(collector != null ? collector.getName() : null)
                .status(pickupRequest.getStatus())
                .requestDate(pickupRequest.getRequestDate())
                .createdAt(pickupRequest.getCreatedAt())
                .build();
    }
}
