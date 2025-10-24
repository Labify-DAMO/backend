package com.labify.backend.user.dto;

import com.labify.backend.inviterequest.entity.InviteRequest;
import com.labify.backend.inviterequest.entity.InviteStatus;
import lombok.Getter;

@Getter
public class UserFacilityResponseDto {
    private final Long userId;
    private final Long requestId;
    private final InviteStatus status;
    private final Long facilityId;
    private final String facilityName;

    private UserFacilityResponseDto(InviteRequest entity) {
        this.userId = entity.getUser().getUserId();
        this.requestId = entity.getId();
        this.status = entity.getStatus();
        this.facilityId = entity.getFacility().getFacilityId();
        this.facilityName = entity.getFacility().getName();
    }

    public static UserFacilityResponseDto from(InviteRequest entity) {
        return new UserFacilityResponseDto(entity);
    }
}
