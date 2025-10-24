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
     * 특정 사용자가 소속된 Facility에 있는 모든 Lab 목록을 조회하는 쿼리 (수정본)
     */
    @Query("SELECT l FROM Lab l WHERE l.facility = (SELECT u.facility FROM User u WHERE u.userId = :userId)")
    List<Lab> findLabsByUserId(@Param("userId") Long userId);
}