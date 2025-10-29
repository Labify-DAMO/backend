package com.labify.backend.config;

import com.labify.backend.waste.entity.*;
import com.labify.backend.waste.repository.WasteCategoryRepository;
import com.labify.backend.waste.repository.WasteTypeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WasteCategorySeeder {

    private final WasteCategoryRepository wasteCategoryRepository;
    private final WasteTypeRepository wasteTypeRepository;

    @PostConstruct
    @Transactional
    public void init() {
        seedCategories();
        seedTypes();
    }

    private void seedCategories() {
        Set<String> existing = wasteCategoryRepository.findAll().stream()
                .map(WasteCategory::getName)
                .collect(Collectors.toSet());

        List<WasteCategory> toInsert = Arrays.stream(WasteCategoryEnum.values())
                .filter(e -> !existing.contains(e.getName()))
                .map(e -> {
                    WasteCategory c = new WasteCategory();
                    c.setName(e.getName());
                    return c;
                })
                .toList();

        if (!toInsert.isEmpty()) {
            wasteCategoryRepository.saveAll(toInsert);
        }
    }

    private void seedTypes() {
        Map<String, WasteCategory> categoryMap = wasteCategoryRepository.findAll().stream()
                .collect(Collectors.toMap(WasteCategory::getName, c -> c));

        Set<String> existing = wasteTypeRepository.findAll().stream()
                .map(WasteType::getName)
                .collect(Collectors.toSet());

        List<WasteType> toInsert = Arrays.stream(WasteTypeEnum.values())
                .filter(e -> !existing.contains(e.getName()))
                .map(e -> {
                    WasteType type = new WasteType();
                    type.setName(e.getName());
                    type.setUnit(e.getUnit());
                    type.setWasteCategory(categoryMap.get(e.getCategory().getName()));
                    return type;
                })
                .toList();

        if (!toInsert.isEmpty()) {
            wasteTypeRepository.saveAll(toInsert);
        }
    }
}
