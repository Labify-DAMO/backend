package com.labify.backend.qr.repository;

import com.labify.backend.disposal.entity.DisposalItem;
import com.labify.backend.qr.entity.Qr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QrRepository extends JpaRepository<Qr,Long> {

    Optional<Qr> findByCode(String code);

    boolean existsByDisposalItem(DisposalItem disposalItem);

    /**
     * QR 코드를 통해 연결된 Pickup의 collector ID를 조회
     */
    @Query("SELECT p.collector.userId FROM Qr q " +
            "JOIN q.disposalItem di " +
            "JOIN PickupRequestItem pri ON pri.disposalItem = di " +
            "JOIN pri.pickupRequest pr " +
            "JOIN Pickup p ON p.pickupRequest = pr " +
            "WHERE q.code = :qrCode")
    Optional<Long> findCollectorIdByQrCode(@Param("qrCode") String qrCode);
}
