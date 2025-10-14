package com.labify.backend.qr.log.repository;

import com.labify.backend.qr.log.entity.QrScanLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QrScanLogRepository extends JpaRepository<QrScanLog,Long> {
}
