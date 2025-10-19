package com.labify.backend.qr.controller;

import com.labify.backend.qr.dto.QrRequestDto;
import com.labify.backend.qr.dto.QrResponseDto;
import com.labify.backend.qr.entity.Qr;
import com.labify.backend.qr.service.QrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/qr")
@RequiredArgsConstructor
public class QrController {

    private final QrService qrService;

    // [POST] /qr
    // qr 코드 조회
    @PostMapping
    public ResponseEntity<QrResponseDto> createQr(@RequestBody QrRequestDto dto) {
        Qr qr = qrService.createQr(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(QrResponseDto.from(qr));
    }
}
