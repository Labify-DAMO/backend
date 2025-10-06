package com.labify.backend.qr.log.repository;

import com.labify.backend.qr.log.entity.QRScanLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QRScanLogRepository extends JpaRepository<QRScanLog,Long> {
}
