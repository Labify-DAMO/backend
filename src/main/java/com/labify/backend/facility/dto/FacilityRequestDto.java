package com.labify.backend.facility.dto;

import com.labify.backend.facility.entity.FacilityType;
import lombok.*;

@Getter
@Setter
public class FacilityRequestDto {
    private FacilityType type;
    private String name;
    private String address;
    private Long managerId;
}