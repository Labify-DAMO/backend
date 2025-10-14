package com.labify.backend.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AiPredictionResponseDto {

    @JsonProperty("is_bio")
    private boolean isBio;  // 바이오 폐기물 여부

    private String coarse;  // 대분류
    private String fine;    // 소분류

    @JsonProperty("is_ocr")
    private boolean isOcr;  // 텍스트 존재 여부

    @JsonProperty("ocr_text")
    private String ocrText; // 검출된 텍스트
}