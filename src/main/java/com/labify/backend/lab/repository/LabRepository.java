package com.labify.backend.lab.repository;

import com.labify.backend.lab.entity.Lab;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabRepository extends JpaRepository<Lab, Long> {

    /**
     * 특정 사용자가 UserFacilityRelation을 통해 속해있는 Facility의 모든 Lab 목록을 조회하는 쿼리
     */
    @Query("SELECT l FROM Lab l JOIN UserFacilityRelation ufr ON l.facility = ufr.facility WHERE ufr.user.userId = :userId")
    List<Lab> findLabsByUserId(@Param("userId") Long userId);
}