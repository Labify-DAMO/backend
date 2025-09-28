package com.labify.backend.lab.request.controller;

import com.labify.backend.lab.dto.LabResponseDto;
import com.labify.backend.lab.entity.Lab;
import com.labify.backend.lab.request.dto.LabRequestCreateDto;
import com.labify.backend.lab.request.dto.LabRequestResponseDto;
import com.labify.backend.lab.request.entity.LabRequest;
import com.labify.backend.lab.request.service.LabRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/labs/requests")
@RequiredArgsConstructor
public class LabRequestController {

    private final LabRequestService labRequestService;

    // [POST] /labs/requests
    @PostMapping
    public ResponseEntity<LabRequestResponseDto> createLabRequest(@RequestBody LabRequestCreateDto requestDto) {
        LabRequest newRequest = labRequestService.createLabRequest(requestDto);
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
}