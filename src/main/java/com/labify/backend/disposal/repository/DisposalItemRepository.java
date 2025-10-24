package com.labify.backend.disposal.repository;

import com.labify.backend.disposal.entity.DisposalItem;
import com.labify.backend.disposal.entity.DisposalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisposalItemRepository extends JpaRepository<DisposalItem, Long> {

    // 하나의 Pickup Id를 사용해 해당 Pickup과 관련있는 DisposalItem들을 모두 조회
    @Query("SELECT pri.disposalItem FROM PickupRequestItem pri WHERE pri.pickupRequest.pickup.id = :pickupId")
    List<DisposalItem> findDisposalItemsByPickupId(@Param("pickupId") Long pickupId);

    // 특정 Pickup ID에 속한 모든 DisposalItem의 상태(status) 목록을 조회
    @Query("SELECT di.status FROM DisposalItem di " +
            "JOIN PickupRequestItem pri ON di.id = pri.disposalItem.id " +
            "JOIN pri.pickupRequest pr " +
            "WHERE pr.pickup.id = :pickupId")
    List<DisposalStatus> findStatusesByPickupId(@Param("pickupId") Long pickupId);
}
