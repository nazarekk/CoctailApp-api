package com.netcracker.coctail.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateRecipe {
    private final String name;
    private final boolean alcohol;
    private final boolean sugarless;
    private final boolean isActive;
}