package com.labify.backend.pickup.request.repository;

import com.labify.backend.pickup.request.entity.PickupRequestItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PickupRequestItemRepository extends JpaRepository<PickupRequestItem, Long> {
}
