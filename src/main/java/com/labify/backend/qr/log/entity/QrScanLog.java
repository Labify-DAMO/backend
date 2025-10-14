package com.labify.backend.qr.log.entity;

import com.labify.backend.qr.entity.Qr;
import com.labify.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class QrScanLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qr_id")
    private Qr qr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scanner_id")
    private User scanner;

    @CreatedDate
    private LocalDateTime scannedAt;

    private boolean isSuccess;
    // 우선 lab_id 뺀 상태
}