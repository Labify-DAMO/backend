package com.labify.backend.waste.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WasteCategoryEnum {
    SOFT_TEXTILE("soft_textile"),
    SHARPS("sharps"),
    TUBES_VIALS("tubes_vials"),
    OTHER("other");

    private final String name;
}
