package com.labify.backend.pickup.request.service;

import com.labify.backend.pickup.entity.PickupStatus;
import com.labify.backend.pickup.repository.PickupRepository;
import com.labify.backend.pickup.request.dto.PickupRequestDetailDto;
import com.labify.backend.pickup.request.dto.PickupRequestSummaryDto;
import com.labify.backend.pickup.request.entity.PickupRequest;
import com.labify.backend.pickup.request.entity.PickupRequestStatus;
import com.labify.backend.pickup.request.repository.PickupRequestRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PickupRequestService {

    private final PickupRequestRepository pickupRequestRepository;
    private final PickupRepository pickupRepository;

    // requesterId를 받아 해당 사용자의 요청을 모두 조회
    // params로 status 값이 들어올 경우, 필터링 진행 (없을 경우 모두 반환)
    @Transactional(readOnly = true)
    public List<PickupRequestDetailDto> findMyPickupRequests(Long requesterId, PickupRequestStatus status) {

        List<PickupRequest> requests;

        // 1. status 파라미터가 null이면 (=필터링이 없으면)
        if (status == null) {
            // 기존의 전체 조회 메서드를 호출
            requests = pickupRequestRepository.findDetailsByRequesterId(requesterId);
        }
        // 2. status 파라미터가 null이 아니면 (=필터링이 있으면)
        else {
            // 새로 만든 상태 필터링 메서드를 호출
            requests = pickupRequestRepository.findDetailsByRequesterIdAndStatus(requesterId, status);
        }

        // 3. 조회된 결과를 DTO로 변환하여 반환
        return requests.stream()
                .map(PickupRequestDetailDto::new)
                .collect(Collectors.toList());
    }

    // 특정 수거 요청만 조회
    @Transactional(readOnly = true)
    public PickupRequestDetailDto findPickupRequestById(Long requestId) {
        // 1. Repository의 findById()를 호출하여 데이터 조회
        PickupRequest request = pickupRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("해당 수거 요청을 찾을 수 없습니다. ID: " + requestId));

        // 2. 조회된 엔티티를 DTO로 변환하여 반환
        return new PickupRequestDetailDto(request);
    }

    // 특정 수거 요청 취소
    @Transactional
    public PickupRequestSummaryDto cancelPickupRequestById(Long requestId) {
        // 1. Repository의 findById()를 호출하여 데이터 조회
        PickupRequest request = pickupRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("해당 수거 요청을 찾을 수 없습니다. ID: " + requestId));

        // 2. status를 변경
        request.setStatus(PickupRequestStatus.CANCELED);

        // 3. 연관된 수거 작업(Pickup)이 있다면 조회해서 상태 변경
        // 수거 작업이 아직 배정되지 않아 Pickup이 없을 수도 있으므로 Optional로 안전하게 처리
        pickupRepository.findByPickupRequestId(requestId).ifPresent(pickup -> {
            pickup.setStatus(PickupStatus.CANCELED);
        });

        // 4. status가 변경된 엔티티를 DTO로 변환하여 반환
        return new PickupRequestSummaryDto(request);
    }
}