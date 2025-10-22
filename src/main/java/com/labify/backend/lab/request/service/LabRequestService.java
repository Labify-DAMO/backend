package com.labify.backend.lab.request.service;

import com.labify.backend.facility.entity.Facility;
import com.labify.backend.facility.relation.entity.Relationship;
import com.labify.backend.facility.relation.repository.RelationshipRepository;
import com.labify.backend.facility.repository.FacilityRepository;
import com.labify.backend.lab.entity.Lab;
import com.labify.backend.lab.repository.LabRepository;
import com.labify.backend.lab.request.dto.LabRequestCreateDto;
import com.labify.backend.lab.request.dto.LabRequestResponseDto;
import com.labify.backend.lab.request.entity.LabRequest;
import com.labify.backend.lab.request.entity.RequestStatus;
import com.labify.backend.lab.request.repository.LabRequestRepository;
import com.labify.backend.notification.service.NotificationService;
import com.labify.backend.user.entity.User;
import com.labify.backend.user.repository.UserRepository;
import com.labify.backend.userfacilityrelation.entity.UserFacilityRelation;
import com.labify.backend.userfacilityrelation.repository.UserFacilityRelationRepository;
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
    private final UserFacilityRelationRepository userFacilityRelationRepository;
    private final LabRepository labRepository;
    private final RelationshipRepository relationshipRepository;

    private final NotificationService notificationService;

    // 실험실 개설 요청 + 알림
    @Transactional
    public LabRequest createLabRequest(User manager, LabRequestCreateDto dto) {
        if (manager == null) {
            throw new IllegalStateException("관리자 정보를 찾을 수 없습니다.");
        }
        UserFacilityRelation relation = userFacilityRelationRepository.findByUser(manager);
        Facility facility = relation.getFacility();

        LabRequest labRequest = new LabRequest();
        labRequest.setFacility(facility);
        labRequest.setManager(manager); // 요청자
        labRequest.setName(dto.getName());
        labRequest.setLocation(dto.getLocation());
        labRequest.setStatus(RequestStatus.PENDING); // 최초 상태는 PENDING
        labRequest.setCreatedAt(LocalDateTime.now());

        LabRequest saved = labRequestRepository.save(labRequest);

        // Relationship 통해 수거업체 찾기
        Relationship relationship = relationshipRepository.findByLabFacility(facility)
                .orElseThrow(() -> new IllegalStateException("이 연구소와 연결된 수거업체를 찾을 수 없습니다."));

        Facility pickupFacility = relationship.getPickupFacility();
        User pickupManager = pickupFacility.getManager();

        if (pickupManager == null) {
            throw new IllegalStateException("수거업체에 매니저가 설정되어 있지 않습니다.");
        }

        // 수거업체 매니저에게 알림 전송
        notificationService.sendLabRequestNotification(pickupManager, saved);

        return saved;
    }

    @Transactional
    public Lab confirmLabRequest(Long requestId) {
        LabRequest request = labRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("실험실을 찾을 수 없습니다"));

        if (!request.getStatus().equals(RequestStatus.PENDING)) {
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
        return request;
    }
}