package com.labify.backend.lab.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LabUpdateRequestDto {
    private String name;
    private String location;
}