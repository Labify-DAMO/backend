package com.labify.backend.disposal.dto;

import com.labify.backend.disposal.entity.DisposalItem;
import lombok.Getter;

@Getter
public class DisposalItemSimpleDto {
    private final Long disposalId;
    private final String wasteTypeName;
    private final Double weight;
    private final String unit;

    public DisposalItemSimpleDto(DisposalItem entity) {
        this.disposalId = entity.getId();
        this.wasteTypeName = entity.getWasteType().getName(); // WasteType 이름
        this.weight = entity.getWeight();
        this.unit = entity.getUnit();
    }
}