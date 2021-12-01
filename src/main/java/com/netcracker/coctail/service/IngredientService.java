package com.netcracker.coctail.service;
import com.netcracker.coctail.model.CreateIngredient;
import com.netcracker.coctail.model.Ingredient;

import java.util.List;

public interface IngredientService {
    Ingredient getIngredientById(Long id);
    List<Ingredient> getIngredientByName(String name);
    void addIngredient(CreateIngredient ingredient);
    void editIngredient(Ingredient ingredient);
    void removeIngredient(long id);
}