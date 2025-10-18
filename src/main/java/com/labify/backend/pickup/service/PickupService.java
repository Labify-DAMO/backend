package com.labify.backend.pickup.service;

import com.labify.backend.disposal.entity.DisposalItem;
import com.labify.backend.notification.service.NotificationService;
import com.labify.backend.pickup.dto.PickupSummaryDto;
import com.labify.backend.pickup.dto.ScanRequestDto;
import com.labify.backend.pickup.dto.ScanResponseDto;
import com.labify.backend.pickup.entity.Pickup;
import com.labify.backend.pickup.entity.PickupStatus;
import com.labify.backend.pickup.repository.PickupRepository;
import com.labify.backend.qr.entity.Qr;
import com.labify.backend.qr.log.entity.QrScanLog;
import com.labify.backend.qr.log.repository.QrScanLogRepository;
import com.labify.backend.qr.repository.QrRepository;
import com.labify.backend.user.entity.User;
import com.labify.backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.labify.backend.disposal.entity.DisposalStatus.PICKED_UP;
import static com.labify.backend.pickup.entity.PickupStatus.COMPLETED;

@Service
@RequiredArgsConstructor
public class PickupService {

    private final QrRepository qrRepository;
    private final UserRepository userRepository;
    private final PickupRepository pickupRepository;
    private final QrScanLogRepository qrScanLogRepository;
    private final NotificationService notificationService;

    @Transactional
    public ScanResponseDto processScan(Long userId, ScanRequestDto dto) {

        // 1. QR 코드로 QR 엔티티 조회
        Qr qr = qrRepository.findByCode(dto.getCode())
                .orElseThrow(() -> new EntityNotFoundException("유효하지 않은 QR 코드입니다."));

        // 2. 수거 담당자 엔티티 조회
        Long collectorId = qrRepository.findCollectorIdByQrCode(qr.getCode())
                .orElseThrow(() -> new EntityNotFoundException("해당 QR에 배정된 수거 담당자가 없습니다.")); // 담당자가 아직 배정되지 않은 경우 예외 처리

        User collector = userRepository.findById(collectorId)
                .orElseThrow(() -> new EntityNotFoundException("수거 담당자 정보를 찾을 수 없습니다."));

        // 2-1. 수거 담당자와 사용자가 동일한지 조회
        User loginUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자 정보가 잘못 전달되었습니다."));

        if (!collector.equals(loginUser)) { // 잘못된 사용자가 스캔할 경우
            QrScanLog log = new QrScanLog();
            log.setQr(qr);
            log.setScannedAt(LocalDateTime.now());
            log.setScanner(loginUser);
            log.setSuccess(false);
            qrScanLogRepository.save(log);  // 실패 로그 남기기
            throw new IllegalArgumentException("수거 담당자가 아닙니다.");
        };

        // 2-2. 이미 수거 처리된 상태인지 체크 (qr_scan_log 테이블에 값이 있는지 확인)
        if (qrScanLogRepository.existsByQrAndIsSuccess(qr, true)) {
            throw new IllegalStateException("이미 수거 처리된 폐기물입니다.");
        }

        // 3. 스캔 로그 기록
        QrScanLog log = new QrScanLog();
        log.setQr(qr);
        log.setScannedAt(LocalDateTime.now());
        log.setScanner(collector);
        log.setSuccess(true);
        qrScanLogRepository.save(log);

        // 4. QR과 연결된 DisposalItem 업데이트
        DisposalItem item = qr.getDisposalItem();
        if (item == null) {
            throw new IllegalStateException("QR 코드에 연결된 폐기물이 없습니다.");
        }

        // 5. 폐기물 상태를 '수거 완료'로 변경
        item.setStatus(PICKED_UP);

        // 6. 폐기물과 연관된 기존 '수거 기록(Pickup)'을 조회
        Pickup pickup = pickupRepository.findPickupByDisposalItemId(item.getId())
                .orElseThrow(() -> new IllegalStateException("해당 폐기물에 대한 수거 기록을 찾을 수 없습니다."));

        // 7. 수거 기록 Pickup 업데이트
        pickup.setCollector(collector);
        pickup.setProcessedAt(LocalDateTime.now());
        pickup.setStatus(COMPLETED);

        // 8. 알림 전송 (요청자에게 수거 완료 알림)
        User requester = pickup.getPickupRequest().getRequester();
        notificationService.sendPickupCompletedNotification(requester, pickup);

        // 9. DTO 반환
        return new ScanResponseDto(item.getId(), item.getStatus().toString(), pickup.getProcessedAt());
    }

    public List<PickupSummaryDto> getPickupsForDate(LocalDate date, Long userId) {
        List<Pickup> pickups = pickupRepository.findPickupsByDateAndUser(date, userId);
        return pickups.stream()
                .map(PickupSummaryDto::new)
                .collect(Collectors.toList());
    }

    // 전체 수거 처리 이력 조회
    @Transactional(readOnly = true)
    public List<PickupSummaryDto> getPickupHistory(Long userId) {
        // processedAt 필드를 기준으로 내림차순(최신순)으로 모든 Pickup 데이터를 정렬하여 조회
        List<Pickup> pickups = pickupRepository.findAllByCollectorUserIdOrderByProcessedAtDesc(userId);

        return pickups.stream()
                .map(PickupSummaryDto::new)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public Pickup updatePickupStatus(Long pickupId, PickupStatus newStatus) {
        Pickup pickup = pickupRepository.findById(pickupId)
                .orElseThrow(() -> new EntityNotFoundException("수거 작업을 찾을 수 없습니다."));
        pickup.setStatus(newStatus);
        return pickup;
    }
}