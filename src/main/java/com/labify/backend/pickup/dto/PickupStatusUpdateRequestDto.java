// PickupStatusUpdateRequestDto.java
package com.labify.backend.pickup.dto;

import com.labify.backend.pickup.entity.PickupStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PickupStatusUpdateRequestDto {
    private PickupStatus status;
}