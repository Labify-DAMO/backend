package com.labify.backend.qr.dto;

import com.labify.backend.qr.entity.Qr;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class QrResponseDto {
    private Long qrId;
    private String code;
    private Long disposalItemId;
    private LocalDateTime createdAt;

    public static QrResponseDto from(Qr qr) {
        return QrResponseDto.builder()
                .qrId(qr.getId())
                .code(qr.getCode())
                .disposalItemId(qr.getDisposalItem().getId())
                .createdAt(qr.getCreatedAt())
                .build();
    }
}
