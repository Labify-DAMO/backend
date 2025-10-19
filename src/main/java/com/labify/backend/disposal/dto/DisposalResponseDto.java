package com.labify.backend.disposal.dto;

import com.labify.backend.disposal.entity.DisposalItem;
import com.labify.backend.disposal.entity.DisposalStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class DisposalResponseDto {
    private Long id;
    private String labName;
    private String wasteTypeName;
    private double weight;
    private String unit;
    private String memo;
    private DisposalStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime availableUntil;

    public static DisposalResponseDto from(DisposalItem item) {
        return DisposalResponseDto.builder()
                .id(item.getId())
                .labName(item.getLab().getName())
                .wasteTypeName(item.getWasteType().getName())
                .weight(item.getWeight())
                .unit(item.getUnit())
                .memo(item.getMemo())
                .status(item.getStatus())
                .createdAt(item.getCreatedAt())
                .availableUntil(item.getAvailableUntil())
                .build();
    }
}

