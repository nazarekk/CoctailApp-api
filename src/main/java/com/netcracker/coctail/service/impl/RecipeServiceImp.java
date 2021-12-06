package com.netcracker.coctail.service.impl;

import com.netcracker.coctail.dao.FriendlistDao;
import com.netcracker.coctail.dao.IngredientDao;
import com.netcracker.coctail.dao.KitchenwareDao;
import com.netcracker.coctail.dao.RecipeDao;
import com.netcracker.coctail.exceptions.InvalidEmailOrPasswordException;
import com.netcracker.coctail.model.CreateRecipe;
import com.netcracker.coctail.model.Ingredient;
import com.netcracker.coctail.model.Kitchenware;
import com.netcracker.coctail.model.Recipe;
import com.netcracker.coctail.service.RecipeService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Data
public class RecipeServiceImp implements RecipeService {

    private final RecipeDao recipeDao;
    private final IngredientDao ingredientDao;
    private final KitchenwareDao kitchenwareDao;
    private final FriendlistDao friendlistDao;

    @Override
    public List<Recipe> getRecipesByName(String name) {
        List<Recipe> result = recipeDao.findAllRecipesByName(name);
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
    public void likeRecipe(String ownerEmail, int recipeId) {
        long ownerId = friendlistDao.getOwnerId(ownerEmail);
        if (!recipeDao.checkLike(ownerId, recipeId)) {
            recipeDao.likeRecipe(recipeId);
        } else {
            log.info("You already liked this recipe");
            throw new InvalidEmailOrPasswordException();
        }
    }

    @Override
    public void withdrawLike(String ownerEmail, int recipeId) {
        long ownerId = friendlistDao.getOwnerId(ownerEmail);
        if (recipeDao.checkLike(ownerId, recipeId)) {
            recipeDao.withdrawLike(recipeId);
        } else {
            log.info("You didn't like this recipe");
            throw new InvalidEmailOrPasswordException();
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
