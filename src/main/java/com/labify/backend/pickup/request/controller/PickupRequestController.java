package com.labify.backend.pickup.request.controller;

import com.labify.backend.pickup.request.dto.PickupRequestDetailDto;
import com.labify.backend.pickup.request.dto.PickupRequestDto;
import com.labify.backend.pickup.request.dto.PickupRequestResponseDto;
import com.labify.backend.pickup.request.dto.PickupRequestSummaryDto;
import com.labify.backend.pickup.request.entity.PickupRequest;
import com.labify.backend.pickup.request.entity.PickupRequestStatus;
import com.labify.backend.pickup.request.service.PickupRequestService;
import com.labify.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pickup-requests")
@RequiredArgsConstructor
public class PickupRequestController {

    private final PickupRequestService pickupRequestService;

    // [POST] /pickup-requests 수거 요청 생성 API + 알림
    @PostMapping("/requests")
    public ResponseEntity<PickupRequestResponseDto> createPickupRequest(
            @RequestBody PickupRequestDto dto) {

        PickupRequest savedRequest = pickupRequestService.createPickupRequest(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(PickupRequestResponseDto.from(savedRequest));
    }

    // [GET] /pickup-requests 또는 /pickup-requests?status=CANCELED 내 수거 요청 모두 조회
    @GetMapping
    public ResponseEntity<List<PickupRequestDetailDto>> findMyPickupRequests(
            @AuthenticationPrincipal User user, @RequestParam(required = false) PickupRequestStatus status) {
        Long currentUserId = user.getUserId();

        List<PickupRequestDetailDto> requests = pickupRequestService.findMyPickupRequests(currentUserId, status);
        return ResponseEntity.ok(requests);
    }

    // [GET] /pickup-requests/{pickup_request_id} 특정 수거 요청 조회
    @GetMapping("/{id}")
    public ResponseEntity<PickupRequestDetailDto> getPickupRequest(@PathVariable("id") Long requestId) {
        PickupRequestDetailDto detailDto = pickupRequestService.findPickupRequestById(requestId);
        return ResponseEntity.ok(detailDto);
    }

    // [PATCH] /pickup-requests/{pickup_request_id}/cancel 특정 수거 요청 취소
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<PickupRequestSummaryDto> cancelPickupRequest(@PathVariable("id") Long requestId) {
        PickupRequestSummaryDto summaryDto = pickupRequestService.cancelPickupRequestById(requestId);
        return ResponseEntity.ok(summaryDto);
    }

}
