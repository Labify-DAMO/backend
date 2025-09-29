package com.labify.backend.userfacilityrelation.repository;

import com.labify.backend.userfacilityrelation.entity.UserFacilityRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFacilityRelationRepository extends JpaRepository<UserFacilityRelation, Long> {
}
