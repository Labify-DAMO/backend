package com.labify.backend.qr.service;

import com.labify.backend.disposal.entity.DisposalItem;
import com.labify.backend.disposal.repository.DisposalItemRepository;
import com.labify.backend.qr.dto.QrRequestDto;
import com.labify.backend.qr.entity.Qr;
import com.labify.backend.qr.repository.QrRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QrService {

    private final QrRepository qrRepository;
    private final DisposalItemRepository disposalItemRepository;

    @Transactional
    public Qr createQr(QrRequestDto dto) {
        // 폐기물 조회
        DisposalItem disposalItem = disposalItemRepository.findById(dto.getDisposalItemId())
                .orElseThrow(() -> new EntityNotFoundException("폐기물을 찾을 수 없습니다."));

        // 이미 QR이 존재하는지 검사
        if (qrRepository.existsByDisposalItem(disposalItem)) {
            throw new IllegalStateException("이 폐기물에는 이미 QR이 생성되었습니다.");
        }

        // QR 코드 문자열 생성
        String code = generateQrCode();

        // QR 엔티티 생성 및 저장
        Qr qr = new Qr();
        qr.setDisposalItem(disposalItem);
        qr.setCode(code);
        qr.setCreatedAt(LocalDateTime.now());

        return qrRepository.save(qr);
    }

    private String generateQrCode() {
        return "QR-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}