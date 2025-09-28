package com.labify.backend.lab.request.service;

import com.labify.backend.facility.entity.Facility;
import com.labify.backend.facility.repository.FacilityRepository;
import com.labify.backend.lab.request.dto.LabRequestCreateDto;
import com.labify.backend.lab.request.entity.LabRequest;
import com.labify.backend.lab.request.entity.RequestStatus;
import com.labify.backend.lab.request.repository.LabRequestRepository;
import com.labify.backend.user.entity.User;
import com.labify.backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LabRequestService {

    private final LabRequestRepository labRequestRepository;
    private final FacilityRepository facilityRepository;
    private final UserRepository userRepository;

    @Transactional
    public LabRequest createLabRequest(LabRequestCreateDto dto) {
        Facility facility = facilityRepository.findById(dto.getFacilityId())
                .orElseThrow(() -> new EntityNotFoundException("Facility not found"));
        User manager = userRepository.findById(dto.getManagerId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        LabRequest labRequest = new LabRequest();
        labRequest.setFacility(facility);
        labRequest.setManager(manager);
        labRequest.setName(dto.getName());
        labRequest.setLocation(dto.getLocation());
        labRequest.setStatus(RequestStatus.PENDING); // 최초 상태는 PENDING
        labRequest.setCreatedAt(LocalDateTime.now());

        return labRequestRepository.save(labRequest);
    }
}