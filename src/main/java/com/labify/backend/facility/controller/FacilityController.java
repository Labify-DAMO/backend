package com.labify.backend.facility.controller;

import com.labify.backend.facility.dto.FacilityInfoResponseDto;
import com.labify.backend.facility.dto.FacilityRequestDto;
import com.labify.backend.facility.dto.FacilityResponseDto;
import com.labify.backend.facility.entity.Facility;
import com.labify.backend.facility.service.FacilityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/facilities")
public class FacilityController {

    private final FacilityService facilityService;

    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    // [POST] 시설 등록
    @PostMapping("/register")
    public ResponseEntity<FacilityResponseDto> registerFacility(@RequestBody FacilityRequestDto facilityRequestDto) {
        Facility newFacility = facilityService.registerFacility(facilityRequestDto);
        // 성공 시, 상태 코드 201과 생성된 facility 정보 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(FacilityResponseDto.from(newFacility));
    }

    // [GET] /facilities/{code}
    @GetMapping("/{code}")
    public ResponseEntity<FacilityInfoResponseDto> getFacilityInfoFromCode(
            @PathVariable String code) {
        Facility facility = facilityService.getFacilityByCode(code);
        return ResponseEntity.ok(FacilityInfoResponseDto.from(facility));
    }
}
