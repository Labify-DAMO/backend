package com.labify.backend.inviterequest.dto;

import com.labify.backend.inviterequest.entity.InviteRequest;
import com.labify.backend.inviterequest.entity.InviteStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class InviteRequestListDto {
    private final Long id;          // 요청 ID
    private final String userName;  // 요청자 이름
    private final LocalDateTime createdAt; // 요청 생성 시각
    private final InviteStatus status;    // 요청 상태

    private InviteRequestListDto(InviteRequest entity) {
        this.id = entity.getId();
        this.userName = entity.getUser().getName();
        this.createdAt = entity.getCreatedAt();
        this.status = entity.getStatus();
    }

    public static InviteRequestListDto from(InviteRequest entity) {
        return new InviteRequestListDto(entity);
    }
}