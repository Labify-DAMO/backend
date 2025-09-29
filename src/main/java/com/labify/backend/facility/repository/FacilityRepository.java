package com.labify.backend.facility.repository;

import com.labify.backend.facility.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    boolean existsByFacilityCode(String facilityCode);

    // 코드로 시설 id 조회
    Optional<Facility> findByFacilityCode(String facilityCode);
}
