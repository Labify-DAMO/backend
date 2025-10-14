package com.labify.backend.ai.controller;

import com.labify.backend.ai.dto.AiPredictionResponseDto;
import com.labify.backend.ai.service.AiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequestMapping("/ai-predict")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping
    public Mono<ResponseEntity<AiPredictionResponseDto>> predictImage(
            @RequestParam("file") MultipartFile file) throws IOException {

        return aiService.predictImage(file)
                .map(ResponseEntity::ok); // 서비스 결과를 성공(200 OK) 응답으로 감싸서 반환
    }
}