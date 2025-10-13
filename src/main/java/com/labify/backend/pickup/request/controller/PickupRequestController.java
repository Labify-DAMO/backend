package com.labify.backend.pickup.request.controller;

import com.labify.backend.pickup.request.dto.PickupRequestDetailDto;
import com.labify.backend.pickup.request.entity.PickupRequestStatus;
import com.labify.backend.pickup.request.service.PickupRequestService;
import com.labify.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pickup-requests")
@RequiredArgsConstructor
public class PickupRequestController {

    private final PickupRequestService pickupRequestService;

    // [GET] /pickup-requests 또는 /pickup-requests?status=CANCELED 내 수거 요청 모두 조회
    @GetMapping
    public ResponseEntity<List<PickupRequestDetailDto>> findMyPickupRequests(
            @AuthenticationPrincipal User user, @RequestParam(required = false) PickupRequestStatus status) {
        Long currentUserId = user.getUserId();

        List<PickupRequestDetailDto> requests = pickupRequestService.findMyPickupRequests(currentUserId, status);
        return ResponseEntity.ok(requests);
    }
}
