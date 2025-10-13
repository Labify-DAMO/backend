package com.labify.backend.pickup.request.repository;

import com.labify.backend.pickup.request.entity.PickupRequest;
import com.labify.backend.pickup.request.entity.PickupRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PickupRequestRepository extends JpaRepository<PickupRequest, Long> {
    // 1. requesterId로 모든 수거 요청을 상세 정보와 함께 조회 (N+1 문제 해결)
    @Query("SELECT DISTINCT pr FROM PickupRequest pr " +
            "LEFT JOIN FETCH pr.items pri " +
            "LEFT JOIN FETCH pri.disposalItem di " +
            "LEFT JOIN FETCH di.wasteType wt " +
            "WHERE pr.requester.userId = :requesterId")
    List<PickupRequest> findDetailsByRequesterId(@Param("requesterId") Long requesterId);

    // 2. 모든 수거 요청 조회 시, status로 필터링하여 조회
    @Query("SELECT DISTINCT pr FROM PickupRequest pr " +
            "LEFT JOIN FETCH pr.items pri " +
            "LEFT JOIN FETCH pri.disposalItem di " +
            "LEFT JOIN FETCH di.wasteType wt " +
            "WHERE pr.requester.userId = :requesterId AND pr.status = :status")
    List<PickupRequest> findDetailsByRequesterIdAndStatus(@Param("requesterId") Long requesterId, @Param("status") PickupRequestStatus status);
}
