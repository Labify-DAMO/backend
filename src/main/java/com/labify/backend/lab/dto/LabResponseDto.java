package com.labify.backend.lab.dto;

import com.labify.backend.lab.entity.Lab;
import lombok.Getter;

@Getter
public class LabResponseDto {
    private final Long labId;
    private final String name;
    private final String location;
    private final Long facilityId;

    private LabResponseDto(Lab entity) {
        this.labId = entity.getLabId();
        this.name = entity.getName();
        this.location = entity.getLocation();
        this.facilityId = entity.getFacility().getFacilityId();
    }

    public static LabResponseDto from(Lab entity) {
        return new LabResponseDto(entity);
    }
}