package com.labify.backend.disposal.entity;

import com.labify.backend.lab.entity.Lab;
import com.labify.backend.user.entity.User;
import com.labify.backend.waste.entity.WasteType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class DisposalItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_id", nullable = false)
    private Lab lab;

    // N:1 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waste_type_id", nullable = false)
    private WasteType wasteType;

    @Column(nullable = false)
    private double weight;

    @Column(nullable = false)
    private String unit;    // 단위: "개", "kg" 등

    private String memo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DisposalStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @CreatedDate
    private LocalDateTime createdAt;

    private LocalDateTime availableUntil;
}
