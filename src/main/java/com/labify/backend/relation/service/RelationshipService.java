package com.labify.backend.relation.service;

import com.labify.backend.facility.entity.Facility;
import com.labify.backend.facility.repository.FacilityRepository;
import com.labify.backend.relation.dto.RelationshipRequestDto;
import com.labify.backend.relation.entity.Relationship;
import com.labify.backend.relation.repository.RelationshipRepository;
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
        // 1. DTO에 담겨온 연구소 시설 ID로 Facility 조회
        Facility labFacility = facilityRepository.findById(dto.getLabFacilityId())
                .orElseThrow(() -> new EntityNotFoundException("LabFacility Not Found"));

        // 2. DTO에 담겨온 수거업체 시설 ID로 Facility 조회
        Facility pickupFacility = facilityRepository.findById(dto.getPickupFacilityId())
                .orElseThrow(() -> new EntityNotFoundException("PickupFacility Not Found"));

        // 3. 관계 설정
        Relationship relationship = new Relationship();
        relationship.setLabFacility(labFacility);
        relationship.setPickupFacility(pickupFacility);
        
        // 4. 엔티티 저장 및 반환
        return relationshipRepository.save(relationship);
    }
}
