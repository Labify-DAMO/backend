package com.labify.backend.relation.dto;

import com.labify.backend.relation.entity.Relationship;
import lombok.Getter;

@Getter
public class RelationshipResponseDto {

    private final Long relationshipId;
    private final Long labFacilityId;
    private final Long pickupFacilityId;

    private RelationshipResponseDto(Relationship entity) {
        this.relationshipId = entity.getRelationshipId();
        this.labFacilityId = entity.getLabFacility().getFacilityId();
        this.pickupFacilityId = entity.getPickupFacility().getFacilityId();
    }

    public static RelationshipResponseDto from(Relationship entity) {
        return new RelationshipResponseDto(entity);
    }
}
