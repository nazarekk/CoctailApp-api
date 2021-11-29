package com.netcracker.coctail.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import com.netcracker.coctail.model.Ingredient;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IngredientDto {
    private String name;
    private String type;
    private String category;


    public static IngredientDto fromIngredient(Ingredient ingredient) {
        IngredientDto ingredientDto = new IngredientDto();
        ingredientDto.setName(ingredient.getName());
        ingredientDto.setType(ingredient.getType());
        ingredientDto.setCategory(ingredient.getCategory());

        return ingredientDto;
    }
}
