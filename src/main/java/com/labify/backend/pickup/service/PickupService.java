package com.labify.backend.pickup.service;

import com.google.zxing.NotFoundException;
import com.labify.backend.disposal.entity.DisposalItem;
import com.labify.backend.disposal.entity.DisposalStatus;
import com.labify.backend.disposal.repository.DisposalItemRepository;
import com.labify.backend.notification.service.NotificationService;
import com.labify.backend.pickup.dto.PickupSummaryDto;
import com.labify.backend.pickup.dto.ScanRequestDto;
import com.labify.backend.pickup.dto.ScanResponseDto;
import com.labify.backend.pickup.entity.Pickup;
import com.labify.backend.pickup.entity.PickupStatus;
import com.labify.backend.pickup.repository.PickupRepository;
import com.labify.backend.pickup.request.entity.PickupRequest;
import com.labify.backend.pickup.request.entity.PickupRequestStatus;
import com.labify.backend.qr.entity.Qr;
import com.labify.backend.qr.log.entity.QrScanLog;
import com.labify.backend.qr.log.repository.QrScanLogRepository;
import com.labify.backend.qr.repository.QrRepository;
import com.labify.backend.qr.service.QrDecoder;
import com.labify.backend.user.entity.User;
import com.labify.backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PickupService {

    private final QrRepository qrRepository;
    private final UserRepository userRepository;
    private final PickupRepository pickupRepository;
    private final QrScanLogRepository qrScanLogRepository;
    private final NotificationService notificationService;
    private final DisposalItemRepository disposalItemRepository;
    private final QrDecoder qrDecoder;

    // 업로드 된 QR 이미지에서 코드 문자열을 decode해서 스캔
    @Transactional
    public ScanResponseDto processScanImage(Long userId, MultipartFile file) {
        final String decodedCode;
        try {
            decodedCode = qrDecoder.decode(file); // PNG/JPG 등에서 문자열 꺼냄
        } catch (NotFoundException e) {
            throw new IllegalArgumentException("QR 코드를 인식하지 못했습니다.");
        } catch (IOException e) {
            throw new RuntimeException("이미지 처리 중 오류가 발생했습니다.");
        }
        return processScan(userId, new ScanRequestDto(decodedCode));
    }

    // QR 스캔 과정
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

        // 4. QR과 연결된 DisposalItem을 활용해 pickup~pickup_request~disposal_item 상태 처리
        DisposalItem item = qr.getDisposalItem();
        if (item == null) {
            throw new IllegalStateException("QR 코드에 연결된 폐기물이 없습니다.");
        }

        // 5. 폐기물 상태를 '수거 완료'로 변경
        item.setStatus(DisposalStatus.PICKED_UP);

        // 6. 폐기물과 연관된 기존 '수거 기록(Pickup)'을 조회
        Pickup pickup = pickupRepository.findPickupByDisposalItemId(item.getId())
                .orElseThrow(() -> new IllegalStateException("해당 폐기물에 대한 수거 기록을 찾을 수 없습니다."));

        // 6-1. 수거 기록을 PROCESSED 상태로 변경
        if (!PickupStatus.PROCESSING.equals(pickup.getStatus())) {
            pickup.setStatus(PickupStatus.PROCESSING);
        }

        // 7. 이 Pickup에 속한 모든 폐기물이 수거 완료되었는지 확인
        checkAndCompletePickupIfAllItemsCollected(pickup);

        // 8. 알림 전송 (요청자에게 수거 완료 알림)
        User requester = pickup.getPickupRequest().getRequester();
        notificationService.sendPickupCompletedNotification(requester, pickup);

        // 9. DTO 반환
        return new ScanResponseDto(item.getId(), item.getStatus().toString(), pickup.getProcessedAt());
    }

    /**
     * 특정 Pickup에 속한 모든 DisposalItem들이 수거 완료(COLLECTED) 상태인지 확인하고,
     * 모두 완료되었다면 Pickup과 PickupRequest의 상태를 COMPLETED로 변경하는 private 메서드
     */
    private void checkAndCompletePickupIfAllItemsCollected(Pickup pickup) {
        // 1. 이 Pickup에 속한 모든 DisposalItem들의 상태를 조회
        List<DisposalStatus> allItemStatuses = disposalItemRepository.findStatusesByPickupId(pickup.getId());

        // 2. 모든 상태가 PICKED_UP인지 확인
        // 하나라도 COLLECTED가 아닌 상태가 있다면, 아직 작업이 완료되지 않은 것이므로 메서드를 종료
        for (DisposalStatus status : allItemStatuses) {
            if (status != DisposalStatus.PICKED_UP) {
                return; // 아직 수거할 폐기물이 남아있으므로 아무것도 하지 않음
            }
        }

        // 3. (모든 폐기물이 수거 완료된 경우) Pickup과 PickupRequest의 상태를 COMPLETED로 변경
        pickup.setStatus(PickupStatus.COMPLETED);
        pickup.getPickupRequest().setStatus(PickupRequestStatus.COMPLETED);

        // 이 메서드는 @Transactional 안에서 호출되므로 변경 사항은 자동으로 저장
    }

    // pickup들을 날짜에 따라 조회
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
        // pickup 상태 변경
        Pickup pickup = pickupRepository.findById(pickupId)
                .orElseThrow(() -> new EntityNotFoundException("수거 작업을 찾을 수 없습니다."));
        pickup.setStatus(newStatus);

        /* pickup과 연관된 pickupRequest의 상태와
          pickupRequest의 DisposalItem 상태도 변경 */
        PickupRequest request = pickup.getPickupRequest();
        List<DisposalItem> disposalItems = disposalItemRepository.findDisposalItemsByPickupId(pickupId);

        System.out.println("request: " + request);
        System.out.println("disposalItems: " + disposalItems);

        PickupRequestStatus pickupRequestStatus;
        DisposalStatus disposalStatus;

        // 픽업 상태가 완료 또는 취소 상태가 됐을 때!!
        if (newStatus.equals(PickupStatus.COMPLETED)) {
            System.out.println("COMPLETED");
            pickupRequestStatus = PickupRequestStatus.COMPLETED;
            disposalStatus = DisposalStatus.PICKED_UP;
        } else if (newStatus.equals(PickupStatus.CANCELED)) {
            System.out.println("CANCELED");
            pickupRequestStatus = PickupRequestStatus.CANCELED;
            disposalStatus = DisposalStatus.STORED;
        } else throw new IllegalStateException("잘못된 상태값입니다.");

        request.setStatus(pickupRequestStatus);
        for (DisposalItem item : disposalItems) {
            item.setStatus(disposalStatus);
        }

        return pickup;
    }
}