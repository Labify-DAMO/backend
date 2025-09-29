package com.labify.backend.inviterequest.dto;

import com.labify.backend.inviterequest.entity.InviteRequest;
import com.labify.backend.inviterequest.entity.InviteStatus;
import lombok.Getter;

@Getter
public class InviteRequestResponseDto {
    private final Long requestId;
    private final InviteStatus status;

    private InviteRequestResponseDto(InviteRequest entity) {
        this.requestId = entity.getId();
        this.status = entity.getStatus();
    }

    public static InviteRequestResponseDto from(InviteRequest entity) {
        return new InviteRequestResponseDto(entity);
    }
}