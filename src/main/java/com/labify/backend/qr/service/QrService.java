package com.labify.backend.qr.service;

import com.google.zxing.WriterException;
import com.labify.backend.disposal.entity.DisposalItem;
import com.labify.backend.disposal.repository.DisposalItemRepository;
import com.labify.backend.qr.dto.QrRequestDto;
import com.labify.backend.qr.entity.Qr;
import com.labify.backend.qr.repository.QrRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QrService {

    private final QrRepository qrRepository;
    private final DisposalItemRepository disposalItemRepository;

    // QR 생성 로직
    @Transactional
    public byte[] generateQrAndReturnImage(QrRequestDto dto) throws IOException, WriterException {
        // 폐기물 조회
        DisposalItem disposalItem = disposalItemRepository.findById(dto.getDisposalItemId())
                .orElseThrow(() -> new EntityNotFoundException("폐기물을 찾을 수 없습니다."));

        if (qrRepository.existsByDisposalItem(disposalItem)) {
            throw new IllegalStateException("이 폐기물에는 이미 QR이 생성되었습니다.");
        }

        // QR 코드 문자열 생성
        String code = "QR-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Qr qr = new Qr();
        qr.setDisposalItem(disposalItem);
        qr.setCode(code);
        qr.setCreatedAt(LocalDateTime.now());
        qrRepository.save(qr);

        // QR 이미지(PNG) 생성
        return QrGenerator.generateQrImage(qr.getCode(), 300, 300);
    }

    // QR 이미지 조회 로직
    @Transactional
    public byte[] getQrImageByDisposalItem(Long disposalItemId) throws IOException, WriterException {
        // QR 조회
        Qr qr = qrRepository.findByDisposalItemId(disposalItemId)
                .orElseThrow(() -> new EntityNotFoundException("해당 폐기물의 QR을 찾을 수 없습니다."));

        // QR 이미지 생성
        return QrGenerator.generateQrImage(qr.getCode(), 300, 300);
    }
}
