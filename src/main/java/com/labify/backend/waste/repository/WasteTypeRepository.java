package com.labify.backend.waste.repository;

import com.labify.backend.waste.entity.WasteType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WasteTypeRepository extends JpaRepository<WasteType, Long> {
    Optional<WasteType> findByName(String name);
}
