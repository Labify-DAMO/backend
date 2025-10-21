package com.labify.backend.facility.dto;

import com.labify.backend.facility.entity.Facility;
import com.labify.backend.facility.entity.FacilityType;
import lombok.Getter;

@Getter
public class FacilityInfoResponseDto {

    private final Long facilityId;
    private final FacilityType type;
    private final String name;
    private final String address;

    private FacilityInfoResponseDto(Facility entity) {
        this.facilityId = entity.getFacilityId();
        this.type = entity.getType();
        this.name = entity.getName();
        this.address = entity.getAddress();
    }

    public static FacilityInfoResponseDto from(Facility entity) {
        return new FacilityInfoResponseDto(entity);
    }
}
