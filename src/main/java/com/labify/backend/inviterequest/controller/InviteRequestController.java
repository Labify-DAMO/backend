package com.labify.backend.inviterequest.controller;

import com.labify.backend.inviterequest.dto.InviteRequestCreateDto;
import com.labify.backend.inviterequest.dto.InviteRequestResponseDto;
import com.labify.backend.inviterequest.entity.InviteRequest;
import com.labify.backend.inviterequest.service.InviteRequestService;
import com.labify.backend.user.entity.User;
import com.labify.backend.userfacilityrelation.dto.UserFacilityRelationResponseDto;
import com.labify.backend.userfacilityrelation.entity.UserFacilityRelation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/facilities/requests")
@RequiredArgsConstructor
public class InviteRequestController {

    private final InviteRequestService inviteRequestService;

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
    public ResponseEntity<UserFacilityRelationResponseDto> confirmInviteRequest(@PathVariable Long requestId) {
        UserFacilityRelation relation = inviteRequestService.confirmInviteRequest(requestId);
        return ResponseEntity.ok(UserFacilityRelationResponseDto.from(relation));
    }

    // [PATCH] /facilities/requests/{requestId}/reject
    @PatchMapping("/{requestId}/reject")
    public ResponseEntity<InviteRequestResponseDto> rejectInviteRequest(@PathVariable Long requestId) {
        InviteRequest request = inviteRequestService.rejectInviteRequest(requestId);
        return ResponseEntity.ok(InviteRequestResponseDto.from(request));
    }
}
