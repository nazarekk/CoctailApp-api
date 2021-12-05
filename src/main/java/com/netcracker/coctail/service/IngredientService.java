package com.netcracker.coctail.service;

import com.netcracker.coctail.model.CreateIngredient;
import com.netcracker.coctail.model.Ingredient;

import java.util.List;

public interface IngredientService {
    Ingredient getIngredientById(Long id);

    List<Ingredient> getIngredientByName(String name);

    List<Ingredient> getIngredientFiltered(String type, String category);

    Boolean addIngredient(CreateIngredient ingredient);

    Boolean editIngredient(Ingredient ingredient);

    Boolean removeIngredient(long id);
}