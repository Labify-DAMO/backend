package com.labify.backend.waste.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WasteTypeEnum {

    // soft_textile
    COTTON("Cotton", "kg", WasteCategoryEnum.SOFT_TEXTILE),
    BANDAGE("Bandage", "kg", WasteCategoryEnum.SOFT_TEXTILE),
    GLOVES("Gloves", "pair", WasteCategoryEnum.SOFT_TEXTILE),
    MASK("Mask", "piece", WasteCategoryEnum.SOFT_TEXTILE),
    MEDICAL_CAP("Medical Cap", "piece", WasteCategoryEnum.SOFT_TEXTILE),

    // sharps
    NEEDLE("Needle", "piece", WasteCategoryEnum.SHARPS),
    SCISSORS("Scissors", "piece", WasteCategoryEnum.SHARPS),
    SYRINGE("Syringe", "piece", WasteCategoryEnum.SHARPS),

    // tubes_vials
    IV_TUBE("IV Tube", "piece", WasteCategoryEnum.TUBES_VIALS),
    TEST_TUBE("Test Tube", "piece", WasteCategoryEnum.TUBES_VIALS),
    VIAL("Vial", "piece", WasteCategoryEnum.TUBES_VIALS),

    // other
    GENERAL_WASTE("General Waste", "kg", WasteCategoryEnum.OTHER);

    private final String name;
    private final String unit;
    private final WasteCategoryEnum category;
}