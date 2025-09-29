package com.labify.backend.inviterequest.repository;

import com.labify.backend.facility.entity.Facility;
import com.labify.backend.inviterequest.entity.InviteRequest;
import com.labify.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InviteRequestRepository extends JpaRepository<InviteRequest, Long> {
    // 이미 가입 요청이 존재하는지 조회
    boolean existsByUserAndFacility(User user, Facility facility);
}