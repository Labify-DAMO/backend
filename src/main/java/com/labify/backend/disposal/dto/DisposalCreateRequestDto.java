package com.labify.backend.disposal.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DisposalCreateRequestDto {
    private Long labId; // 사용자가 반드시 한 연구실에만 속하면 로그인 정보에서 받아와도 되는데 그런다는 보장이 없어서 일단 requestDto에 둠
    private Long wasteTypeId;
    private double weight;
    private String unit;
    private String memo;
    private LocalDateTime availableUntil;
}
