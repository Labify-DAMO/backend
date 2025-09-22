package com.labify.backend.lab.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabRequestDto {
    private String name;
    private String location;
    private Long facilityId; // 어느 시설에 속하는지
}