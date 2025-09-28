package com.labify.backend.lab.request.dto;

import com.labify.backend.lab.request.entity.LabRequest;
import lombok.Getter;

@Getter
public class LabRequestResponseDto {
    private final Long requestId;
    private final String status;;

    private LabRequestResponseDto(LabRequest entity) {
        this.requestId = entity.getRequestId();
        this.status = entity.getStatus().name();
    }

    public static LabRequestResponseDto from(LabRequest entity) {
        return new LabRequestResponseDto(entity);
    }
}
