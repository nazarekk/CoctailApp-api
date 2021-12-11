package com.netcracker.coctail.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IngredientRecipe {
    private final Integer id;
    private final Integer recipeId;
    private final Integer ingredientId;
}
