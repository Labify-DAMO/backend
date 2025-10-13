package com.labify.backend.lab.controller;

import com.labify.backend.lab.dto.LabRequestDto;
import com.labify.backend.lab.dto.LabResponseDto;
import com.labify.backend.lab.dto.LabUpdateRequestDto;
import com.labify.backend.lab.entity.Lab;
import com.labify.backend.lab.service.LabService;
import com.labify.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // [PATCH] /labs/{labsId}
    @PatchMapping("/{labId}")
    public ResponseEntity<LabResponseDto> updateLab(
            @PathVariable Long labId,
            @RequestBody LabUpdateRequestDto requestDto) {
        Lab updateLab = labService.updateLab(labId, requestDto);
        LabResponseDto responseDto = LabResponseDto.from(updateLab);
        return ResponseEntity.ok(responseDto);
    }
    
    // [GET] /labs 내가 소속된 모든 실험실 조회
    @GetMapping
    public ResponseEntity<List<LabResponseDto>> findMyLabs(@AuthenticationPrincipal User user) {
        // @AuthenticationPrincipal로 받은 User 객체에서 ID를 사용합니다.
        List<LabResponseDto> labs = labService.findMyLabs(user.getUserId());
        return ResponseEntity.ok(labs);
    }
}