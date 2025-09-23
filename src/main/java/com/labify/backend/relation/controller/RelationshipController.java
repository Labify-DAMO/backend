package com.labify.backend.relation.controller;

import com.labify.backend.relation.dto.RelationshipRequestDto;
import com.labify.backend.relation.dto.RelationshipResponseDto;
import com.labify.backend.relation.entity.Relationship;
import com.labify.backend.relation.service.RelationshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
