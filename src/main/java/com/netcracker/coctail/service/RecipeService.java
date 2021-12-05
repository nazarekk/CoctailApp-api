package com.netcracker.coctail.service;

public interface RecipeService {
    void addRecipe(String name);

    void addIngredientToRecipe(int id, String name);

    void addKitchenwareToRecipe(int id, String name);

    void removeIngredientFromRecipe(int id, String name);

    void removeKitchenwareFromRecipe(int id, String name);
}
