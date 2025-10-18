package com.labify.backend.pickup.repository;

import com.labify.backend.pickup.entity.Pickup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PickupRepository extends JpaRepository<Pickup, Long> {

    // 1. DisposalItem ID를 통해 연관된 Pickup을 찾는 쿼리
    @Query("SELECT p FROM Pickup p JOIN p.pickupRequest pr JOIN pr.items pri WHERE pri.disposalItem.id = :disposalItemId")
    Optional<Pickup> findPickupByDisposalItemId(@Param("disposalItemId") Long disposalItemId);

    // 2. requestDate를 기준으로 Pickup 목록을 찾는 쿼리
    @Query("SELECT p FROM Pickup p JOIN p.pickupRequest pr " +
            "WHERE pr.requestDate = :date " +
            "AND (pr.requester.userId = :userId OR p.collector.userId = :userId)")
    List<Pickup> findPickupsByDateAndUser(@Param("date") LocalDate date, @Param("userId") Long userId);

    // 3. requestDate를 기준으로 Pickup 목록을 찾는 쿼리
    // 아직 구현 X

    // 4. PickupRequest ID를 통해 연관된 Pickup을 찾는 쿼리
    Optional<Pickup> findByPickupRequestId(Long pickupRequestId);

    // 5. 특정 수거 요청 담당자의 수거 기록을 최신순으로 조회
    List<Pickup> findAllByCollectorUserIdOrderByProcessedAtDesc(Long collectorId);
}
