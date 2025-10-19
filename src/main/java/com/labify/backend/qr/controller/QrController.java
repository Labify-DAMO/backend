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
    // QR 생성
    @PostMapping
    public ResponseEntity<byte[]> generateQrAndReturnImage(@RequestBody QrRequestDto dto)
            throws IOException, WriterException {

        byte[] qrImage = qrService.generateQrAndReturnImage(dto);

        return ResponseEntity.ok()
                .header("Content-Type", "image/png")
                .body(qrImage);
    }

    // [GET] /qr/{disposalItemId}/image
    // QR 이미지 조회
    @GetMapping("/{disposalItemId}/image")
    public ResponseEntity<byte[]> getQrImageByDisposalItem(@PathVariable Long disposalItemId)
            throws IOException, WriterException {

        byte[] qrImage = qrService.getQrImageByDisposalItem(disposalItemId);

        return ResponseEntity.ok()
                .header("Content-Type", "image/png") // .contentType(MediaType.IMAGE_PNG) 가 나을수도
                .body(qrImage);
    }
}
