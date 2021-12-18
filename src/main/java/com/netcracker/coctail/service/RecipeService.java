package com.netcracker.coctail.service;

import com.netcracker.coctail.model.CreateRecipe;
import com.netcracker.coctail.model.DishRecipe;
import com.netcracker.coctail.model.Recipe;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

public interface RecipeService {

    Integer addRecipe(CreateRecipe recipe);

    boolean addIngredientToRecipe(int id, String name);

    boolean addKitchenwareToRecipe(int id, String name);

    boolean removeIngredientFromRecipe(int id, String name);

    boolean removeKitchenwareFromRecipe(int id, String name);

    List<DishRecipe> getRecipesByName(String name, HttpServletRequest httpServletRequest);

    List<DishRecipe> getRecipesFiltered(String sugarless, String alcohol, HttpServletRequest httpServletRequest);

    DishRecipe getRecipeById(int id);

    boolean editRecipe(Recipe recipe);

    boolean removeRecipe(int id);

    boolean addToFavourites(String ownerEmail, int recipeId, boolean favourite);

    boolean likeRecipe(String ownerEmail, int recipeId, boolean liked);

}
