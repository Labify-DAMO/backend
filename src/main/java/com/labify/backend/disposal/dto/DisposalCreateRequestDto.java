package com.labify.backend.disposal.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DisposalCreateRequestDto {
    private Long labId;

    private String wasteTypeName; // AI에서 받은 fine 값
    private double weight;
    private String unit;
    private String memo;
    private LocalDateTime availableUntil;
}
