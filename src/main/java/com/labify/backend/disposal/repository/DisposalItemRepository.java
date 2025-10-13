package com.labify.backend.disposal.repository;

import com.labify.backend.disposal.entity.DisposalItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisposalItemRepository extends JpaRepository<DisposalItem, Long> {
}
