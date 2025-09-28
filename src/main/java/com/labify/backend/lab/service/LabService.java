package com.labify.backend.lab.service;

import com.labify.backend.facility.entity.Facility;
import com.labify.backend.facility.repository.FacilityRepository;
import com.labify.backend.lab.dto.LabRequestDto;
import com.labify.backend.lab.dto.LabUpdateRequestDto;
import com.labify.backend.lab.entity.Lab;
import com.labify.backend.lab.repository.LabRepository;
import com.labify.backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 만듦
public class LabService {

    private final LabRepository labRepository;
    private final FacilityRepository facilityRepository;
    private final UserRepository userRepository;

    @Transactional
    public Lab registerLab(LabRequestDto dto) {
        Facility facility = facilityRepository.findById(dto.getFacilityId())
                .orElseThrow(() -> new EntityNotFoundException("해당 시설을 찾을 수 없습니다."));

        Lab lab = new Lab();
        lab.setName(dto.getName());
        lab.setLocation(dto.getLocation());
        lab.setFacility(facility);

        return labRepository.save(lab);
    }

    @Transactional
    public Lab updateLab(Long labId, LabUpdateRequestDto dto) {
        Lab existingLab = labRepository.findById(labId)
                .orElseThrow(() -> new EntityNotFoundException("해당 실험실을 찾을 수 없습니다."));

        // DTO에 값이 있는 필드만 선택적으로 업데이트
        if (dto.getName() != null) {
            existingLab.setName(dto.getName());
        }
        if (dto.getLocation() != null) {
            existingLab.setLocation(dto.getLocation());
        }

        return existingLab;
    }
}