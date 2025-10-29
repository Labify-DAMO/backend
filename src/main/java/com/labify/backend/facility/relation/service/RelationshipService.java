package com.labify.backend.facility.relation.service;

import com.labify.backend.facility.entity.Facility;
import com.labify.backend.facility.relation.dto.RelationResponseDto;
import com.labify.backend.facility.repository.FacilityRepository;
import com.labify.backend.facility.relation.dto.RelationshipRequestDto;
import com.labify.backend.facility.relation.entity.Relationship;
import com.labify.backend.facility.relation.repository.RelationshipRepository;
import com.labify.backend.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RelationshipService {

    private final RelationshipRepository relationshipRepository;
    private final FacilityRepository facilityRepository;

    @Transactional
    public Relationship createRelationship(RelationshipRequestDto dto) {
        Facility labFacility = facilityRepository.findById(dto.getLabFacilityId())
                .orElseThrow(() -> new EntityNotFoundException("LabFacility Not Found"));
        Facility pickupFacility = facilityRepository.findById(dto.getPickupFacilityId())
                .orElseThrow(() -> new EntityNotFoundException("PickupFacility Not Found"));

        boolean alreadyExists = relationshipRepository.existsByLabFacilityAndPickupFacility(labFacility, pickupFacility);
        if (alreadyExists) {
            throw new IllegalStateException("이미 해당 시설들 사이의 관계가 존재합니다.");
        }

        Relationship relationship = new Relationship();
        relationship.setLabFacility(labFacility);
        relationship.setPickupFacility(pickupFacility);

        return relationshipRepository.save(relationship);
    }

    @Transactional
    public void deleteRelationship(Long relationshipId) {
        Relationship relationship = relationshipRepository.findById(relationshipId)
                .orElseThrow(() -> new EntityNotFoundException("관계를 찾을 수 없습니다. ID: " + relationshipId));

        relationshipRepository.delete(relationship);
    }

    @Transactional
    public RelationResponseDto getMyFacilityRelationship(User user) {
        Relationship relationship = relationshipRepository.findRelationshipByUserId(user.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("해당 사용자가 소속된 시설의 관계를 찾을 수 없습니다. ID: " + user.getUserId()));

        return RelationResponseDto.from(relationship);
    }
}
