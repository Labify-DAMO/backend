package com.labify.backend.inviterequest.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class InviteRequestListWrapperDto {

    private final List<InviteRequestListDto> requests;
    private final int count; // 목록 개수

    private InviteRequestListWrapperDto(List<InviteRequestListDto> requests) {
        this.requests = requests;
        this.count = requests.size();
    }

    public static InviteRequestListWrapperDto from(List<InviteRequestListDto> requests) {
        return new InviteRequestListWrapperDto(requests);
    }
}