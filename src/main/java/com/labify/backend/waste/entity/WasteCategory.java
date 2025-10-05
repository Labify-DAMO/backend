package com.labify.backend.waste.entity; // waste 패키지를 새로 만듭니다.

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class WasteCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    // WasteCategory(1)가 여러 WasteType(N)을 가질 수 있음을 의미
    @OneToMany(mappedBy = "wasteCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WasteType> wasteTypes = new ArrayList<>();
}