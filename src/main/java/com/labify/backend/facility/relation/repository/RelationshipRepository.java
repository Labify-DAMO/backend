package com.labify.backend.facility.relation.repository;

import com.labify.backend.facility.entity.Facility;
import com.labify.backend.facility.relation.entity.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Long> {

    // 연구소와 수거 업체 사이 관계가 이미 존재하는지 검증하는 메서드
    boolean existsByLabFacilityAndPickupFacility(Facility labFacility, Facility pickupFacility);

    // 실험실 정보 가지고 관계 찾는 메서드
    Optional<Relationship> findByLabFacility(Facility labFacility);

    // 사용자 정보를 통해 관계 정보를 가져오는 메서드
    @Query("SELECT r FROM Relationship r WHERE r.labFacility = (SELECT u.facility FROM User u WHERE u.userId = :userId) OR r.pickupFacility = (SELECT u.facility FROM User u WHERE u.userId = :userId)")
    Optional<Relationship> findRelationshipByUserId(@Param("userId") Long userId);
}