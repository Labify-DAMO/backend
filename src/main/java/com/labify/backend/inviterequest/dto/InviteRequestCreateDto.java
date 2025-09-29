package com.labify.backend.inviterequest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InviteRequestCreateDto {
    private Long userId;
    private String facilityCode;
}