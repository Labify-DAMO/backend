package com.labify.backend.lab.request.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class LabRequestListResponseDto {

    private final List<LabRequestItemDto> requests;
    private final long count; // 전체 요청 개수

    private LabRequestListResponseDto(List<LabRequestItemDto> requests) {
        this.requests = requests;
        this.count = requests.size();
    }

    public static LabRequestListResponseDto from(List<LabRequestItemDto> requests) {
        return new LabRequestListResponseDto(requests);
    }
}