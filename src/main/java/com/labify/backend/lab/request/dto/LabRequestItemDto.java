package com.labify.backend.lab.request.dto;

import com.labify.backend.lab.request.entity.LabRequest;
import com.labify.backend.lab.request.entity.RequestStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LabRequestItemDto {
    private final Long requestId;    // 요청 ID
    private final String labName;      // 요청된 실험실 이름
    private final String location;     // 요청된 위치
    private final String managerName;  // 요청자 이름
    private final LocalDateTime createdAt; // 요청 생성 시각
    private final RequestStatus status;    // 요청 상태

    // 생성자를 private으로 선언
    private LabRequestItemDto(LabRequest entity) {
        this.requestId = entity.getRequestId();
        this.labName = entity.getName();
        this.location = entity.getLocation();
        this.managerName = entity.getManager().getName();
        this.createdAt = entity.getCreatedAt();
        this.status = entity.getStatus();
    }

    // LabRequest 엔티티를 DTO로 변환하는 정적 팩토리 메서드
    public static LabRequestItemDto from(LabRequest entity) {
        return new LabRequestItemDto(entity);
    }
}