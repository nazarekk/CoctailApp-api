package com.netcracker.coctail.service;
import com.netcracker.coctail.model.Ingredient;

public interface IngredientService{
    Ingredient getIngredientById(Long id);
    Ingredient getIngredientByName(String name);
    void addIngredient(Ingredient ingredient);
    void editIngredient(Ingredient ingredient);
    void removeIngredient(Ingredient ingredient);
}