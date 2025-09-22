package com.labify.backend.lab.service;

import com.labify.backend.facility.entity.Facility;
import com.labify.backend.facility.repository.FacilityRepository;
import com.labify.backend.lab.dto.LabRequestDto;
import com.labify.backend.lab.entity.Lab;
import com.labify.backend.lab.repository.LabRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 만듦
public class LabService {

    private final LabRepository labRepository;
    private final FacilityRepository facilityRepository;

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
}