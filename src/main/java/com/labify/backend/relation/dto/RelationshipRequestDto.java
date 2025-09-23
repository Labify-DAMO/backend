package com.labify.backend.relation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RelationshipRequestDto {

    private Long labFacilityId;
    private Long pickupFacilityId;
}
