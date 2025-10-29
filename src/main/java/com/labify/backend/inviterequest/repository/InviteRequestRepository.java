package com.labify.backend.inviterequest.repository;

import com.labify.backend.facility.entity.Facility;
import com.labify.backend.inviterequest.entity.InviteRequest;
import com.labify.backend.inviterequest.entity.InviteStatus;
import com.labify.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InviteRequestRepository extends JpaRepository<InviteRequest, Long> {
    // 이미 가입 요청이 존재하는지 조회
    boolean existsByUserAndFacility(User user, Facility facility);

    // 사용자가 관리하는 시설의 가입 요청 조회 (status 필터링)
    @Query("""
        SELECT ir 
        FROM InviteRequest ir 
        WHERE ir.facility = (
            SELECT u.facility 
            FROM User u 
            WHERE u.userId = :userId
        )
        AND (:status IS NULL OR ir.status = :status)
    """)
    List<InviteRequest> findInviteRequestsForMyFacility(
            @Param("userId") Long userId,
            @Param("status") InviteStatus status
    );

}