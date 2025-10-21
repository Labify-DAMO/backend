package com.labify.backend.inviterequest.service;

import com.labify.backend.facility.entity.Facility;
import com.labify.backend.facility.repository.FacilityRepository;
import com.labify.backend.inviterequest.dto.InviteRequestCreateDto;
import com.labify.backend.inviterequest.dto.InviteRequestResponseDto;
import com.labify.backend.inviterequest.entity.InviteRequest;
import com.labify.backend.inviterequest.entity.InviteStatus;
import com.labify.backend.inviterequest.repository.InviteRequestRepository;
import com.labify.backend.userfacilityrelation.entity.UserFacilityRelation;
import com.labify.backend.userfacilityrelation.repository.UserFacilityRelationRepository;
import com.labify.backend.user.entity.User;
import com.labify.backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InviteRequestService {

    private final InviteRequestRepository inviteRequestRepository;
    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;
    private final UserFacilityRelationRepository userFacilityRelationRepository;

    @Transactional
    public InviteRequest createInviteRequest(Long userId, InviteRequestCreateDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Facility facility = facilityRepository.findByFacilityCode(dto.getFacilityCode())
                .orElseThrow(() -> new EntityNotFoundException("Facility not found"));

        // 동일한 사용자가 동일한 시설에 보낸 요청이 있는지 확인
        if (inviteRequestRepository.existsByUserAndFacility(user, facility)) {
            throw new IllegalStateException("이미 해당 시설에 가입을 요청했습니다.");
        }

        InviteRequest inviteRequest = new InviteRequest();
        inviteRequest.setUser(user);
        inviteRequest.setFacility(facility);
        inviteRequest.setStatus(InviteStatus.PENDING);
        inviteRequest.setCreatedAt(LocalDateTime.now());

        return inviteRequestRepository.save(inviteRequest);
    }

    @Transactional
    public UserFacilityRelation confirmInviteRequest(Long requestId) {
        InviteRequest request =  inviteRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        if (!request.getStatus().equals(InviteStatus.PENDING)) {
            throw new IllegalStateException("이미 처리된 요청입니다.");
        }

        request.setStatus(InviteStatus.CONFIRMED);

        UserFacilityRelation relation = new UserFacilityRelation();
        relation.setUser(request.getUser());
        relation.setFacility(request.getFacility());

        return userFacilityRelationRepository.save(relation);
    }

    @Transactional
    public InviteRequest rejectInviteRequest(Long requestId) {
        InviteRequest request =  inviteRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        if (!request.getStatus().equals(InviteStatus.PENDING)) {
            throw new IllegalStateException("이미 처리된 요청입니다.");
        }

        request.setStatus(InviteStatus.REJECTED);
        return request;
    }
}