package com.labify.backend.qr.entity;

import com.labify.backend.disposal.entity.DisposalItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Qr {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disposal_item_id", unique = true)
    private DisposalItem disposalItem;

    @Column(unique = true, nullable = false)
    private String code; // "QR123456789"와 같은 실제 QR 코드 값

    @CreatedDate
    private LocalDateTime createdAt;
}
