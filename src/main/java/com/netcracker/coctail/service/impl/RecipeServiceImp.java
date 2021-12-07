package com.netcracker.coctail.service.impl;

import com.netcracker.coctail.dao.FriendlistDao;
import com.netcracker.coctail.dao.IngredientDao;
import com.netcracker.coctail.dao.KitchenwareDao;
import com.netcracker.coctail.dao.RecipeDao;
import com.netcracker.coctail.exceptions.InvalidEmailOrPasswordException;
import com.netcracker.coctail.model.*;
import com.netcracker.coctail.service.RecipeService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@Data
public class RecipeServiceImp implements RecipeService {

    private final RecipeDao recipeDao;
    private final IngredientDao ingredientDao;
    private final KitchenwareDao kitchenwareDao;
    private final FriendlistDao friendlistDao;

    @Override
    public List<DishRecipe> getRecipesByName(String name) {
        List<Recipe> recipes = recipeDao.findAllRecipesByName(name);
        List<DishRecipe> result = new ArrayList<>();
        for (Recipe recipe : recipes){
            int recipeId = recipe.getId();
            result.add(new DishRecipe(
                    recipe.getId(),
                    recipe.getName(),
                    recipe.getRating(),
                    recipe.isAlcohol(),
                    recipe.isSugarless(),
                    recipeDao.containsIngredients(recipeId),
                    recipeDao.containsKitchenware(recipeId)
            ));
        }
        return result;
    }

    @Override
    public Recipe getRecipeById(int id) {
        Recipe result = recipeDao.findRecipeById(id).get(0);
        if (result == null) {
            log.warn("IN getRecipeById - no recipes found by id: {}", id);
            return null;
        }
        return result;
    }

    @Override
    public void editRecipe(Recipe recipe) {
        int id = recipe.getId();
        String name = recipe.getName();
        Recipe result = recipeDao.findRecipeById(id).get(0);
        if (result == null) {
            log.info("Recipe with id " + id + " doesn't exist");
            throw new InvalidEmailOrPasswordException();
        }
        if (recipeDao.findRecipeByName(name).isEmpty()) {
            recipeDao.editRecipe(recipe);
        } else {
            log.info("Recipe with name " + name + " already exists");
            throw new InvalidEmailOrPasswordException();
        }
    }

    @Override
    public void removeRecipe(int id) {
        Recipe result = recipeDao.findRecipeById(id).get(0);
        if (result == null) {
            log.info("Recipe with id " + id + " doesn't exist");
            throw new InvalidEmailOrPasswordException();
        }
        recipeDao.removeRecipe(result.getId());
    }

    @Override
    public void addToFavourites(String ownerEmail, int recipeId) {
        long ownerId = friendlistDao.getOwnerId(ownerEmail);
        recipeDao.addToFavourites(ownerId, recipeId);
    }

    @Override
    public void likeRecipe(String ownerEmail, int recipeId, boolean like) {
        long ownerId = friendlistDao.getOwnerId(ownerEmail);
        if(recipeDao.checkLike(ownerId, recipeId).isEmpty()){
            log.info("To like the dish, you must first add it to your favourites");
            throw new InvalidEmailOrPasswordException();
        }
        if (like && recipeDao.checkLike(ownerId, recipeId).get(0).isLiked()) {
            log.info("You already liked this recipe");
            throw new InvalidEmailOrPasswordException();
        } else if(like){
            recipeDao.likeRecipe(recipeId);
            recipeDao.likedLock(ownerId, recipeId, like);
        } else if (!like && !recipeDao.checkLike(ownerId, recipeId).get(0).isLiked()){
            log.info("You didn't like this recipe");
        } else {
            recipeDao.withdrawLike(recipeId);
            recipeDao.likedLock(ownerId, recipeId, like);
        }
    }

    @Override
    public void addRecipe(CreateRecipe recipe) {
        if (recipeDao.findRecipeByName(recipe.getName()).isEmpty()) {
            recipeDao.createRecipe(recipe);
        } else {
            log.info("Recipe with name " + recipe.getName() + " already exists");
            throw new InvalidEmailOrPasswordException();
        }
    }

    @Override
    public void addIngredientToRecipe(int id, String name) {
        Recipe recipe = recipeDao.findRecipeById(id).get(0);
        Ingredient ingredient = ingredientDao.findIngredientByName(name).get(0);
        if (recipe == null) {
            log.info("Recipe with id " + id + " doesn't exist");
            throw new InvalidEmailOrPasswordException();
        }
        if (ingredient == null) {
            log.info("Ingredient with name " + name + " doesn't exist");
            throw new InvalidEmailOrPasswordException();
        }
        recipeDao.addIngredientToRecipe(recipe.getId(), ingredient.getId());
    }

    @Override
    public void addKitchenwareToRecipe(int id, String name) {
        Recipe recipe = recipeDao.findRecipeById(id).get(0);
        Kitchenware kitchenware = kitchenwareDao.findKitchenwareByName(name).get(0);
        if (recipe == null) {
            log.info("Recipe with id " + id + " doesn't exist");
            throw new InvalidEmailOrPasswordException();
        }
        if (kitchenware == null) {
            log.info("Kitchenware with name " + name + " doesn't exist");
            throw new InvalidEmailOrPasswordException();
        }
        recipeDao.addKitchenwareToRecipe(recipe.getId(), kitchenware.getId());
    }

    @Override
    public void removeIngredientFromRecipe(int id, String name) {
        Recipe recipe = recipeDao.findRecipeById(id).get(0);
        Ingredient ingredient = ingredientDao.findIngredientByName(name).get(0);
        if (recipe == null) {
            log.info("Recipe with id " + id + " doesn't exist");
            throw new InvalidEmailOrPasswordException();
        }
        if (ingredient == null) {
            log.info("Ingredient with name " + name + " doesn't exist");
            throw new InvalidEmailOrPasswordException();
        }
        recipeDao.removeIngredientFromRecipe(recipe.getId(), ingredient.getId());
    }

    @Override
    public void removeKitchenwareFromRecipe(int id, String name) {
        Recipe recipe = recipeDao.findRecipeById(id).get(0);
        Kitchenware kitchenware = kitchenwareDao.findKitchenwareByName(name).get(0);
        if (recipe == null) {
            log.info("Recipe with id " + id + " doesn't exist");
            throw new InvalidEmailOrPasswordException();
        }
        if (kitchenware == null) {
            log.info("Kitchenware with name " + name + " doesn't exist");
            throw new InvalidEmailOrPasswordException();
        }
        recipeDao.removeKitchenwareFromRecipe(recipe.getId(), kitchenware.getId());
    }

}
