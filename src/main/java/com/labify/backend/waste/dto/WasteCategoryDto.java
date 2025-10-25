package com.labify.backend.waste.dto;

import com.labify.backend.waste.entity.WasteCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WasteCategoryDto {
    private Long id;
    private String name;

    public static WasteCategoryDto from(WasteCategory category) {
        return WasteCategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}