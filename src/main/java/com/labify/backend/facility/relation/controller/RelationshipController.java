package com.labify.backend.facility.relation.controller;

import com.labify.backend.facility.relation.dto.RelationResponseDto;
import com.labify.backend.facility.relation.dto.RelationshipRequestDto;
import com.labify.backend.facility.relation.dto.RelationshipResponseDto;
import com.labify.backend.facility.relation.entity.Relationship;
import com.labify.backend.facility.relation.service.RelationshipService;
import com.labify.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/relation")
@RequiredArgsConstructor
public class RelationshipController {

    private final RelationshipService relationshipService;

    // [POST] /relation
    @PostMapping
    public ResponseEntity<RelationshipResponseDto> createRelationship(@RequestBody RelationshipRequestDto requestDto) {
        Relationship newRelationship = relationshipService.createRelationship(requestDto);
        RelationshipResponseDto responseDto = RelationshipResponseDto.from(newRelationship);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // [DELETE] /relation/delete/{relationshipId}
    @DeleteMapping("/delete/{relationshipId}")
    public ResponseEntity<Void> deleteRelationship(@PathVariable Long relationshipId) {
        relationshipService.deleteRelationship(relationshipId);
        return ResponseEntity.noContent().build();
    }

    // [GET] /relation
    @GetMapping
    public ResponseEntity<RelationResponseDto> getMyFacilityRelationship(@AuthenticationPrincipal(expression = "user") User user) {
        RelationResponseDto response = relationshipService.getMyFacilityRelationship(user);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
