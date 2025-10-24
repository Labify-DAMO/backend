package com.labify.backend.disposal.dto;

import com.labify.backend.disposal.entity.DisposalStatus;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class DisposalUpdateRequestDto {
    private Long wasteTypeId;
    private Double weight;
    private String unit;
    private String memo;
    private DisposalStatus status;
    private LocalDateTime availableUntil;
  }