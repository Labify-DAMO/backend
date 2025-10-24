package com.labify.backend.waste.service;

import com.labify.backend.waste.dto.WasteCategoryDto;
import com.labify.backend.waste.dto.WasteTypeDto;
import com.labify.backend.waste.repository.WasteCategoryRepository;
import com.labify.backend.waste.repository.WasteTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WasteCategoryService {

    private final WasteCategoryRepository wasteCategoryRepository;
    private final WasteTypeRepository wasteTypeRepository;

    // 모든 카테고리 조회
    public List<WasteCategoryDto> getAllCategories() {
        return wasteCategoryRepository.findAll().stream()
                .map(WasteCategoryDto::from)
                .collect(Collectors.toList());
    }

    // 특정 카테고리의 타입들 조회
    public List<WasteTypeDto> getTypesByCategory(String categoryName) {
        wasteCategoryRepository.findByName(categoryName)
                .orElseThrow(() -> new EntityNotFoundException("Category not found: " + categoryName));

        return wasteTypeRepository.findByWasteCategory_Name(categoryName).stream()
                .map(WasteTypeDto::from)
                .collect(Collectors.toList());
    }
}