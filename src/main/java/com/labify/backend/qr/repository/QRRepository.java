package com.labify.backend.qr.repository;

import com.labify.backend.qr.entity.QR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QRRepository extends JpaRepository<QR,Long> {
    Optional<QR> findByCode(String code);
}
