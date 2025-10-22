package com.labify.backend.lab.request.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabRequestCreateDto {
    private Long facilityId;
    private String name;
    private String location;
}
