package com.labify.backend.lab.request.repository;

import com.labify.backend.lab.request.entity.LabRequest;
import com.labify.backend.lab.request.entity.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabRequestRepository extends JpaRepository<LabRequest, Long> {

    // 내가 보낸 요청 조회
    @Query("""
        SELECT lr
        FROM LabRequest lr
        WHERE lr.facility = (
            SELECT u.facility
            FROM User u
            WHERE u.userId = :userId
        )
        AND (:status IS NULL OR lr.status = :status)
        ORDER BY lr.createdAt DESC
    """)
    List<LabRequest> findLabRequestsForMyFacility(
            @Param("userId") Long userId,
            @Param("status") RequestStatus status
    );

    // 내가 시설 관리자로 있는 시설의 실험실 요청 조회
    @Query("""
        SELECT lr
        FROM LabRequest lr
        WHERE EXISTS (
            SELECT 1
            FROM Facility f
            JOIN f.manager m
            WHERE f = lr.facility
              AND m.userId = :userId
        )
          AND (:status IS NULL OR lr.status = :status)
        ORDER BY lr.createdAt DESC
    """)
    List<LabRequest> findLabRequestsForFacilitiesIMange(
            @Param("userId") Long userId,
            @Param("status") RequestStatus status
    );
}
