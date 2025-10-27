package com.labify.backend.disposal.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class DisposalListResponseDto {

    private final Long totalCount;
    private final List<DisposalResponseDto> disposalItems;

    @Builder
    public DisposalListResponseDto(Long totalCount, List<DisposalResponseDto> disposalItems) {
        this.totalCount = totalCount;
        this.disposalItems = disposalItems;
    }
}
