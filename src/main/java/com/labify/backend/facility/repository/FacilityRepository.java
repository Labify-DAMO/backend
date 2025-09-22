package com.labify.backend.facility.repository;

import com.labify.backend.facility.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    boolean existsByFacilityCode(String facilityCode);
}
