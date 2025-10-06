package com.labify.backend.pickup.dto;

import com.labify.backend.pickup.entity.Pickup;
import com.labify.backend.pickup.entity.PickupStatus;
import lombok.Getter;

@Getter
public class PickupSummaryDto {
    private final Long pickupId;
    private final String labName;
    private final String labLocation;
    private final String facilityAddress;
    private final PickupStatus status;

    public PickupSummaryDto(Pickup entity) {
        this.pickupId = entity.getId();
        this.labName = entity.getPickupRequest().getLab().getName();
        this.labLocation = entity.getPickupRequest().getLab().getLocation();
        this.facilityAddress = entity.getPickupRequest().getLab().getFacility().getAddress();
        this.status = entity.getStatus();
    }
}