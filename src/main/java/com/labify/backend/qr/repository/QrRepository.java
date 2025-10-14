package com.labify.backend.qr.repository;

import com.labify.backend.disposal.entity.DisposalItem;
import com.labify.backend.qr.entity.Qr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QrRepository extends JpaRepository<Qr,Long> {
    Optional<Qr> findByCode(String code);
    boolean existsByDisposalItem(DisposalItem disposalItem);
}
