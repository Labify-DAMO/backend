package com.labify.backend.lab.controller;

import com.labify.backend.lab.dto.LabRequestDto;
import com.labify.backend.lab.dto.LabResponseDto;
import com.labify.backend.lab.entity.Lab;
import com.labify.backend.lab.service.LabService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/labs")
@RequiredArgsConstructor
public class LabController {

    private final LabService labService;

    // [POST] /labs/register
    @PostMapping("/register")
    public ResponseEntity<LabResponseDto> registerLab(@RequestBody LabRequestDto requestDto) {
        Lab newLab = labService.registerLab(requestDto);
        LabResponseDto responseDto = LabResponseDto.from(newLab);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}