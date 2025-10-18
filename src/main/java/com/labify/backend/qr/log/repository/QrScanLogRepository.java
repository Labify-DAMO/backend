package com.labify.backend.qr.log.repository;

import com.labify.backend.qr.entity.Qr;
import com.labify.backend.qr.log.entity.QrScanLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QrScanLogRepository extends JpaRepository<QrScanLog,Long> {

    /**
     * 특정 QR에 대해 성공(isSuccess=true)한 스캔 기록이 존재하는지 확인
     */
    boolean existsByQrAndIsSuccess(Qr qr, boolean isSuccess);
}
