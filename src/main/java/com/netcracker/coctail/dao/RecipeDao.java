package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.CreateRecipe;
import com.netcracker.coctail.model.Recipe;

import java.util.List;

public interface RecipeDao {

    List<Recipe> findRecipeByName(String name);

    void createRecipe(CreateRecipe recipe);

    List<Recipe> findRecipeById(Integer id);

    void addIngredientToRecipe(Integer recipeId, Long ingredientId);

    void addKitchenwareToRecipe(Integer recipeId, Long kitchenwareId);

    void removeIngredientFromRecipe(Integer recipeId, Long ingredientId);

    void removeKitchenwareFromRecipe(Integer recipeId, Long kitchenwareId);

    List<Recipe> findAllRecipesByName(String name);

    void editRecipe(Recipe recipe);

    void removeRecipe(int id);

    void addToFavourites(long ownerId, int recipeId);

    void likeRecipe(int recipeId);

    boolean checkLike(long userId, int recipeId);

    void withdrawLike(int recipeId);
}
