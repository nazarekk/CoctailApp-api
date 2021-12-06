package com.netcracker.coctail.service;

import com.netcracker.coctail.model.CreateRecipe;
import com.netcracker.coctail.model.Recipe;

import java.util.List;

public interface RecipeService {

    void addRecipe(CreateRecipe recipe);

    void addIngredientToRecipe(int id, String name);

    void addKitchenwareToRecipe(int id, String name);

    void removeIngredientFromRecipe(int id, String name);

    void removeKitchenwareFromRecipe(int id, String name);

    List<Recipe> getRecipesByName(String name);

    Recipe getRecipeById(int id);

    void editRecipe(Recipe recipe);

    void removeRecipe(int id);

    void addToFavourites(String ownerEmail, int recipeId);

    void likeRecipe(String ownerEmail, int recipeId);

    void withdrawLike(String ownerEmail, int recipeId);

}
