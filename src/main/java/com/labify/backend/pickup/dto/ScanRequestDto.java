package com.labify.backend.pickup.dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScanRequestDto {
    private String code;

    public ScanRequestDto(String code) {
        this.code = code;
    }
}