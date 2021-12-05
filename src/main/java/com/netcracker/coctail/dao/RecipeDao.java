package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.Recipe;

import java.util.List;

public interface RecipeDao {

    List<Recipe> findRecipeByName(String name);

    void createRecipe(String name);

    List<Recipe> findRecipeById(Integer id);

    void addIngredientToRecipe(Integer recipeId, Long ingredientId);

    void addKitchenwareToRecipe(Integer recipeId, Long kitchenwareId);

    void removeIngredientFromRecipe(Integer recipeId, Long ingredientId);

    void removeKitchenwareFromRecipe(Integer recipeId, Long kitchenwareId);
}
