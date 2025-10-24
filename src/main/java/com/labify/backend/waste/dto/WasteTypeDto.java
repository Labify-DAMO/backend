package com.labify.backend.waste.dto;

import com.labify.backend.waste.entity.WasteType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WasteTypeDto {
    private Long id;
    private String name;
    private String unit;
    private String categoryName;

    public static WasteTypeDto from(WasteType wasteType) {
        return WasteTypeDto.builder()
                .id(wasteType.getId())
                .name(wasteType.getName())
                .unit(wasteType.getUnit())
                .categoryName(wasteType.getWasteCategory().getName())
                .build();
    }
}