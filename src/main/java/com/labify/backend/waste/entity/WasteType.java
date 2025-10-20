package com.labify.backend.waste.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class WasteType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "waste_type_id")
    private Long id;

    // WasteType(N)이 하나의 WasteCategory(1)에 속함을 의미
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private WasteCategory wasteCategory;

    @Column(nullable = false)
    private String name;

    private String unit; // 예: "kg", "개"
}