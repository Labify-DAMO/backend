package com.labify.backend.waste.controller;

import com.labify.backend.waste.dto.WasteCategoryDto;
import com.labify.backend.waste.dto.WasteTypeDto;
import com.labify.backend.waste.service.WasteCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/waste-categories")
@RequiredArgsConstructor
public class WasteCategoryController {

    private final WasteCategoryService wasteCategoryService;

    // [GET] /waste-categories
    // 모든 카테고리 조회
    @GetMapping
    public ResponseEntity<List<WasteCategoryDto>> getAllCategories() {
        return ResponseEntity.ok(wasteCategoryService.getAllCategories());
    }

    // [GET] /waste-categories/{categoryName}/types
    // 특정 카테고리의 타입들 조회
    @GetMapping("/{categoryName}/types")
    public ResponseEntity<List<WasteTypeDto>> getTypesByCategory(@PathVariable String categoryName) {
        return ResponseEntity.ok(wasteCategoryService.getTypesByCategory(categoryName));
    }
}