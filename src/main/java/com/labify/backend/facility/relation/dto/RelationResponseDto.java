package com.labify.backend.facility.relation.dto;

import com.labify.backend.facility.entity.Facility;
import com.labify.backend.facility.entity.FacilityType;
import com.labify.backend.facility.relation.entity.Relationship;
import lombok.Getter;

@Getter
public class RelationResponseDto {

    private final Long relationshipId;
    private final FacilityInfoDto labFacility; // 연구소 시설 정보
    private final FacilityInfoDto pickupFacility; // 수거 업체 시설 정보

    private RelationResponseDto(Relationship entity) {
        this.relationshipId = entity.getRelationshipId();
        this.labFacility = FacilityInfoDto.from(entity.getLabFacility());
        this.pickupFacility = FacilityInfoDto.from(entity.getPickupFacility());
    }

    public static RelationResponseDto from(Relationship entity) {
        return new RelationResponseDto(entity);
    }

    // 각 시설의 정보를 담을 내부 DTO 클래스
    @Getter
    public static class FacilityInfoDto {
        private final Long facilityId;
        private final String name;
        private final FacilityType type;
        private final String address;

        private FacilityInfoDto(Facility entity) {
            this.facilityId = entity.getFacilityId();
            this.name = entity.getName();
            this.type = entity.getType();
            this.address = entity.getAddress();
        }

        public static FacilityInfoDto from(Facility entity) {
            return new FacilityInfoDto(entity);
        }
    }
}