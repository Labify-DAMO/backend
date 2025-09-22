package com.labify.backend.facility.dto;

import com.labify.backend.facility.entity.Facility;
import com.labify.backend.facility.entity.FacilityType;
import lombok.Getter;

@Getter
public class FacilityResponseDto {

    private final Long facilityId;
    private final String name;
    private final FacilityType type;
    private final String address;
    private final String facilityCode; // 서버에서 생성된 코드

    // 생성자를 private으로 만들어 정적 팩토리 메서드 사용을 유도
    private FacilityResponseDto(Facility entity) {
        this.facilityId = entity.getFacilityId();
        this.name = entity.getName();
        this.type = entity.getType();
        this.address = entity.getAddress();
        this.facilityCode = entity.getFacilityCode();
    }

    // Entity를 DTO로 변환하는 정적 팩토리 메서드
    public static FacilityResponseDto from(Facility entity) {
        return new FacilityResponseDto(entity);
    }
}
