package com.labify.backend.pickup.request.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PickupRequestDto {

    private Long labId;              // 어떤 실험실에서 요청했는지
    private LocalDate requestDate;   // 희망 수거일
    private List<Long> disposalItemIds; // 포함된 폐기물 ID 리스트
}