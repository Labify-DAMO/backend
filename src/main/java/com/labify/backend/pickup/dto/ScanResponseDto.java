package com.labify.backend.pickup.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ScanResponseDto {
    private Long disposalId;
    private String status;
    private LocalDateTime processedAt;
}