package com.labify.backend.ai.service;

import com.labify.backend.ai.dto.AiPredictionResponseDto;
import com.labify.backend.waste.repository.WasteTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AiService {    // 중개자 역할의 클래스

    private final WebClient webClient;
    private final WasteTypeRepository wasteTypeRepository;

    @Transactional
    public Mono<AiPredictionResponseDto> predictImage(MultipartFile file) throws IOException {
        // 1. 이미지 파일 변환 (바이트 배열)
        ByteArrayResource resource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {   // 파일명 전송용
                return file.getOriginalFilename();
            }
        };

        // 2. multipart/form-data 형식의 요청 Body 구성
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", resource); // 이미지 파일 첨부

        // 3. WebClient를 사용하여 AI 서버에 POST 요청 전송
        return webClient.post()
                .uri("/predict") // WebClientConfig에 설정된 baseUrl 뒤에 붙는 경로
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .retrieve() // 응답 받음
                .bodyToMono(AiPredictionResponseDto.class) // 받은 JSON을 DTO로 변환
                .flatMap(response -> {
                    // AI 서버로부터 받은 fine 값으로 WasteType 조회
                    if (response.getFine() != null && !response.getFine().isEmpty()) {
                        return Mono.fromCallable(() ->
                                wasteTypeRepository.findByName(response.getFine())
                                        .map(wasteType -> {
                                            response.setUnit(wasteType.getUnit());
                                            return response;
                                        })
                                        .orElse(response) // WasteType을 찾지 못하면 unit 없이 반환
                        );
                    }
                    return Mono.just(response);
                });

    }
}