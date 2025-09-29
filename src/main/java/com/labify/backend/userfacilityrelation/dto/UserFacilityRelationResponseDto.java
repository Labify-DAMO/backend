package com.labify.backend.userfacilityrelation.dto;

import com.labify.backend.userfacilityrelation.entity.UserFacilityRelation;
import lombok.Getter;

@Getter
public class UserFacilityRelationResponseDto {
    private final Long relationId;
    private final Long userId;
    private final Long facilityId;

    private UserFacilityRelationResponseDto(UserFacilityRelation entity) {
        this.relationId = entity.getId();
        this.userId = entity.getUser().getUserId();
        this.facilityId = entity.getFacility().getFacilityId();
    }

    public static UserFacilityRelationResponseDto from(UserFacilityRelation entity) {
        return new UserFacilityRelationResponseDto(entity);
    }
}