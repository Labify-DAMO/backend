package com.labify.backend.lab.request.controller;

import com.labify.backend.lab.dto.LabResponseDto;
import com.labify.backend.lab.entity.Lab;
import com.labify.backend.lab.request.dto.LabRequestCreateDto;
import com.labify.backend.lab.request.dto.LabRequestListResponseDto;
import com.labify.backend.lab.request.dto.LabRequestResponseDto;
import com.labify.backend.lab.request.entity.LabRequest;
import com.labify.backend.lab.request.entity.RequestStatus;
import com.labify.backend.lab.request.service.LabRequestService;
import com.labify.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/labs/requests")
@RequiredArgsConstructor
public class LabRequestController {

    private final LabRequestService labRequestService;

    // [POST] /labs/requests
    @PostMapping
    public ResponseEntity<LabRequestResponseDto> createLabRequest(
            @AuthenticationPrincipal(expression = "user") User user,
            @RequestBody LabRequestCreateDto requestDto) {
        LabRequest newRequest = labRequestService.createLabRequest(user, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(LabRequestResponseDto.from(newRequest));
    }

    // [PATCH] /labs/requests/{requestId}/confirm
    @PatchMapping("/{requestId}/confirm")
    public ResponseEntity<LabResponseDto> confirmLabRequest(@PathVariable Long requestId) {
        Lab newLab = labRequestService.confirmLabRequest(requestId);
        return ResponseEntity.ok(LabResponseDto.from(newLab));
    }

    // [PATCH] /labs/requests/{requestId}/confirm
    @PatchMapping("/{requestId}/reject")
    public ResponseEntity<LabRequestResponseDto> rejectLabRequest(@PathVariable Long requestId) {
        LabRequest rejectedRequest = labRequestService.rejectLabRequest(requestId);
        return ResponseEntity.ok(LabRequestResponseDto.from(rejectedRequest));
    }

    // [GET] /labs/requests/me/{status}
    @GetMapping("/me/{status}")
    public ResponseEntity<LabRequestListResponseDto> getMyLabRequests(
            @AuthenticationPrincipal(expression = "user") User user, RequestStatus status) {
        return ResponseEntity.ok(labRequestService.getMyLabRequests(user, status));
    }

    // [GET] /labs/requests{status}
    @GetMapping("{status}")
    public ResponseEntity<LabRequestListResponseDto> getAllLabRequests(
            @AuthenticationPrincipal(expression = "user") User user, RequestStatus status) {
        return ResponseEntity.ok(labRequestService.getLabRequests(user, status));
    }
}