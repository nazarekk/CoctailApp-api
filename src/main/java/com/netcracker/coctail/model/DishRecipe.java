package com.netcracker.coctail.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DishRecipe {
    private final Integer id;
    private final String name;
    private Alcohol alcohol;
    private final boolean sugarless;
    private final boolean isActive;
    private final String image;
    private final String recipe;
    private final Integer rating;
    private final List<Ingredient> ingredientList;
    private final List<Kitchenware> kitchenwareList;
}
