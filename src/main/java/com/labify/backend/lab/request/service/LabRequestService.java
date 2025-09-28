package com.labify.backend.lab.request.service;

import com.labify.backend.facility.entity.Facility;
import com.labify.backend.facility.repository.FacilityRepository;
import com.labify.backend.lab.entity.Lab;
import com.labify.backend.lab.repository.LabRepository;
import com.labify.backend.lab.request.dto.LabRequestCreateDto;
import com.labify.backend.lab.request.dto.LabRequestResponseDto;
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
    private final LabRepository labRepository;

    @Transactional
    public LabRequest createLabRequest(LabRequestCreateDto dto) {
        Facility facility = facilityRepository.findById(dto.getFacilityId())
                .orElseThrow(() -> new EntityNotFoundException("Facility not found"));
        User manager = userRepository.findById(dto.getManagerId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        LabRequest labRequest = new LabRequest();
        labRequest.setFacility(facility);
        labRequest.setManager(manager); // 요청자?
        labRequest.setName(dto.getName());
        labRequest.setLocation(dto.getLocation());
        labRequest.setStatus(RequestStatus.PENDING); // 최초 상태는 PENDING
        labRequest.setCreatedAt(LocalDateTime.now());

        return labRequestRepository.save(labRequest);
    }

    @Transactional
    public Lab confirmLabRequest(Long requestId) {
        LabRequest request = labRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("실험실을 찾을 수 없습니다"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 요청입니다.");
        }

        request.setStatus(RequestStatus.CONFIRMED);

        Lab newLab = new Lab();
        newLab.setName(request.getName());
        newLab.setLocation(request.getLocation());
        newLab.setFacility(request.getFacility());

        return labRepository.save(newLab);
    }

    @Transactional
    public LabRequest rejectLabRequest(Long requestId) {
        LabRequest request = labRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("실험실을 찾을 수 없습니다"));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new IllegalStateException("이미 처리된 요청입니다.");
        }

        request.setStatus(RequestStatus.REJECTED);
        return labRequestRepository.save(request);
    }
}