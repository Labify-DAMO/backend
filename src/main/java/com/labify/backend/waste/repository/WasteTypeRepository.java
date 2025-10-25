package com.labify.backend.waste.repository;

import com.labify.backend.waste.entity.WasteType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WasteTypeRepository extends JpaRepository<WasteType, Long> {
    Optional<WasteType> findByName(String name);

    // 특정 카테고리의 모든 타입 조회
    List<WasteType> findByWasteCategory_Name(String categoryName);
}
