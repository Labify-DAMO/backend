package com.labify.backend.lab.request.controller;

import com.labify.backend.lab.request.dto.LabRequestCreateDto;
import com.labify.backend.lab.request.dto.LabRequestResponseDto;
import com.labify.backend.lab.request.entity.LabRequest;
import com.labify.backend.lab.request.service.LabRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/labs/requests")
@RequiredArgsConstructor
public class LabRequestController {

    private final LabRequestService labRequestService;

    @PostMapping
    public ResponseEntity<LabRequestResponseDto> createLabRequest(@RequestBody LabRequestCreateDto requestDto) {
        LabRequest newRequest = labRequestService.createLabRequest(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(LabRequestResponseDto.from(newRequest));
    }
}