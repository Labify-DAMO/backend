package com.labify.backend.inviterequest.service;

import com.labify.backend.common.response.GeneralException;
import com.labify.backend.facility.entity.Facility;
import com.labify.backend.facility.repository.FacilityRepository;
import com.labify.backend.inviterequest.dto.InviteRequestCreateDto;
import com.labify.backend.inviterequest.dto.InviteRequestListDto;
import com.labify.backend.inviterequest.entity.InviteRequest;
import com.labify.backend.inviterequest.entity.InviteStatus;
import com.labify.backend.inviterequest.repository.InviteRequestRepository;
import com.labify.backend.inviterequest.exception.InviteRequestErrorCode;
import com.labify.backend.inviterequest.response.InviteRequestException;
import com.labify.backend.user.dto.UserFacilityResponseDto;
import com.labify.backend.user.entity.User;
import com.labify.backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InviteRequestService {

    private final InviteRequestRepository inviteRequestRepository;
    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;

    @Transactional
    public List<InviteRequestListDto> findInviteRequestsForMyFacility(User user, InviteStatus status) {
        List<InviteRequest> inviteRequests =
                inviteRequestRepository.findInviteRequestsForMyFacility(user.getUserId(), status);

        // InviteRequest 엔티티 리스트를 InviteRequestListDto 리스트로 변환
        return inviteRequests.stream()
                .map(InviteRequestListDto::from) // 각 엔티티를 DTO로 변환
                .collect(Collectors.toList());
    }

    @Transactional
    public InviteRequest createInviteRequest(Long userId, InviteRequestCreateDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Facility facility = facilityRepository.findByFacilityCode(dto.getFacilityCode())
                .orElseThrow(() -> new InviteRequestException(InviteRequestErrorCode.INVALID_FACILITY_CODE));

        // 동일한 사용자가 동일한 시설에 보낸 요청이 있는지 확인
        if (inviteRequestRepository.existsByUserAndFacility(user, facility)) {
            throw new InviteRequestException(InviteRequestErrorCode.ALREADY_REQUESTED);
        }

        InviteRequest inviteRequest = new InviteRequest();
        inviteRequest.setUser(user);
        inviteRequest.setFacility(facility);
        inviteRequest.setStatus(InviteStatus.PENDING);
        inviteRequest.setCreatedAt(LocalDateTime.now());

        return inviteRequestRepository.save(inviteRequest);
    }

    @Transactional
    public UserFacilityResponseDto confirmInviteRequest(Long requestId) {
        InviteRequest request =  inviteRequestRepository.findById(requestId)
                .orElseThrow(() -> new InviteRequestException(InviteRequestErrorCode.REQUEST_NOT_FOUND));

        if (!request.getStatus().equals(InviteStatus.PENDING)) {
            throw new InviteRequestException(InviteRequestErrorCode.ALREADY_REQUESTED);
        }

        request.setStatus(InviteStatus.CONFIRMED);

        User user = request.getUser();
        Facility facility = request.getFacility();
        user.setFacility(facility);

        return UserFacilityResponseDto.from(request);
    }

    @Transactional
    public InviteRequest rejectInviteRequest(Long requestId) {
        InviteRequest request =  inviteRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("Request not found"));

        if (!request.getStatus().equals(InviteStatus.PENDING)) {
            throw new GeneralException(
                    InviteRequestErrorCode.ALREADY_PROCESSED
            );
        }

        request.setStatus(InviteStatus.REJECTED);
        return request;
    }
}