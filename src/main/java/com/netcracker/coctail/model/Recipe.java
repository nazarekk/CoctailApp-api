package com.netcracker.coctail.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Recipe {
    private final Integer id;
    private final String name;
    private Alcohol alcohol;
    private final boolean sugarless;
    private final boolean isActive;
    private final String image;
    private final String recipe;
    private final Integer rating;
}
