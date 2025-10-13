package com.labify.backend.pickup.request.dto;

import com.labify.backend.pickup.request.entity.PickupRequest;
import com.labify.backend.pickup.request.entity.PickupRequestStatus;
import com.labify.backend.user.entity.User;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PickupRequestSummaryDto {
    private final Long pickupRequestId;
    private final Long labId;
    private final LocalDate requestDate;
    private final PickupRequestStatus status;

    public PickupRequestSummaryDto(PickupRequest entity) {
        this.pickupRequestId = entity.getId();
        this.labId = entity.getId();
        this.requestDate = entity.getRequestDate();
        this.status = entity.getStatus();
    }
}