package com.netcracker.coctail.service;

import com.netcracker.coctail.model.CreateIngredient;
import com.netcracker.coctail.model.Ingredient;

import java.util.List;

public interface IngredientService {
    Ingredient getIngredientById(Long id);

    List<Ingredient> getIngredientByName(String name);

    List<Ingredient> getIngredientFiltered(String type, String category);

    void addIngredient(CreateIngredient ingredient);

    void editIngredient(Ingredient ingredient);

    void removeIngredient(long id);
}