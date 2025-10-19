package com.labify.backend.qr.controller;

import com.google.zxing.WriterException;
import com.labify.backend.qr.dto.QrRequestDto;
import com.labify.backend.qr.service.QrService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/qr")
@RequiredArgsConstructor
public class QrController {

    private final QrService qrService;

    // [POST] /qr
    // QR 코드 생성
    @PostMapping
    public ResponseEntity<byte[]> generateQrAndReturnImage(@RequestBody QrRequestDto dto)
            throws IOException, WriterException {

        byte[] qrImage = qrService.generateQrAndReturnImage(dto);

        return ResponseEntity.ok()
                .header("Content-Type", "image/png")
                .body(qrImage);
    }

    // [GET] /qr
    // QR 코드 조회
}
