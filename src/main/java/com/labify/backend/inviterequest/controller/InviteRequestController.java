package com.labify.backend.inviterequest.controller;

import com.labify.backend.inviterequest.dto.InviteRequestCreateDto;
import com.labify.backend.inviterequest.dto.InviteRequestListDto;
import com.labify.backend.inviterequest.dto.InviteRequestListWrapperDto;
import com.labify.backend.inviterequest.dto.InviteRequestResponseDto;
import com.labify.backend.inviterequest.entity.InviteRequest;
import com.labify.backend.inviterequest.entity.InviteStatus;
import com.labify.backend.inviterequest.service.InviteRequestService;
import com.labify.backend.user.dto.UserFacilityResponseDto;
import com.labify.backend.user.entity.Role;
import com.labify.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/facilities/requests")
@RequiredArgsConstructor
public class InviteRequestController {

    private final InviteRequestService inviteRequestService;

    // [GET] /facilities/requests/{status}
    @GetMapping("/{status}")
    public ResponseEntity<InviteRequestListWrapperDto> getFacilityInviteRequests(
            @AuthenticationPrincipal(expression = "user") User user, @PathVariable InviteStatus status) {
        List<InviteRequestListDto> dtoList = inviteRequestService.findInviteRequestsForMyFacility(user, status);
        return ResponseEntity.ok(InviteRequestListWrapperDto.from(dtoList));
    }

    // [POST] /facilities/requests
    @PostMapping
    public ResponseEntity<InviteRequestResponseDto> createInviteRequest(
            @AuthenticationPrincipal(expression = "user") User user,
            @RequestBody InviteRequestCreateDto requestDto) {
        InviteRequest newRequest = inviteRequestService.createInviteRequest(user.getUserId(), requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(InviteRequestResponseDto.from(newRequest));
    }

    // [PATCH] /facilities/requests/{requestId}/confirm
    @PatchMapping("/{requestId}/confirm")
    public ResponseEntity<UserFacilityResponseDto> confirmInviteRequest(@PathVariable Long requestId) {
        UserFacilityResponseDto response = inviteRequestService.confirmInviteRequest(requestId);
        return ResponseEntity.ok(response);
    }

    // [PATCH] /facilities/requests/{requestId}/reject
    @PatchMapping("/{requestId}/reject")
    public ResponseEntity<InviteRequestResponseDto> rejectInviteRequest(@PathVariable Long requestId) {
        InviteRequest request = inviteRequestService.rejectInviteRequest(requestId);
        return ResponseEntity.ok(InviteRequestResponseDto.from(request));
    }
}
