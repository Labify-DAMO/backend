package com.labify.backend.pickup.controller;

import com.labify.backend.pickup.dto.PickupStatusUpdateRequestDto;
import com.labify.backend.pickup.dto.PickupSummaryDto;
import com.labify.backend.pickup.dto.ScanRequestDto;
import com.labify.backend.pickup.dto.ScanResponseDto;
import com.labify.backend.pickup.entity.Pickup;
import com.labify.backend.pickup.service.PickupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/pickups")
@RequiredArgsConstructor
public class PickupController {

    private final PickupService pickupService;

    // [POST] /pickups/scan
    @PostMapping("/scan")
    public ResponseEntity<ScanResponseDto> scanQrCode(@RequestBody ScanRequestDto requestDto) {
        ScanResponseDto responseDto = pickupService.processScan(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    //[GET] /pickups
    @GetMapping
    public ResponseEntity<List<PickupSummaryDto>> getPickupHistory() {
        List<PickupSummaryDto> history = pickupService.getPickupHistory();
        return ResponseEntity.ok(history);
    }

    // [GET] /pickups/tomorrow
    @GetMapping("/tomorrow")
    public ResponseEntity<List<PickupSummaryDto>> getTomorrowsPickups() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<PickupSummaryDto> pickups = pickupService.getPickupsForDate(tomorrow);
        return ResponseEntity.ok(pickups);
    }

    // [GET] /pickup/today
    @GetMapping("/today")
    public ResponseEntity<List<PickupSummaryDto>> getTodaysPickups() {
        LocalDate today = LocalDate.now();
        List<PickupSummaryDto> pickups = pickupService.getPickupsForDate(today);
        return ResponseEntity.ok(pickups);
    }

    // [PATCH] /pickup/{pickupId}/status
    @PatchMapping("/{pickupId}/status")
    public ResponseEntity<PickupSummaryDto> updatePickupStatus(
            @PathVariable Long pickupId,
            @RequestBody PickupStatusUpdateRequestDto requestDto) {

        Pickup updatedPickup = pickupService.updatePickupStatus(pickupId, requestDto.getStatus());
        return ResponseEntity.ok(new PickupSummaryDto(updatedPickup));
    }
}