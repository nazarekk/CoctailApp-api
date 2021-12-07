package com.netcracker.coctail.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Recipe {
    private final Integer id;
    private final String name;
    private final Integer rating;
    private final boolean alcohol;
    private final boolean sugarless;
    private final boolean isActive;
}
