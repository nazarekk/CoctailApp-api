package com.netcracker.coctail.service.impl;

import com.netcracker.coctail.dao.IngredientDao;
import com.netcracker.coctail.dao.KitchenwareDao;
import com.netcracker.coctail.dao.RecipeDao;
import com.netcracker.coctail.exceptions.InvalidEmailOrPasswordException;
import com.netcracker.coctail.model.Ingredient;
import com.netcracker.coctail.model.Kitchenware;
import com.netcracker.coctail.model.Recipe;
import com.netcracker.coctail.service.RecipeService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Data
public class RecipeServiceImp implements RecipeService {

    private final RecipeDao recipeDao;
    private final IngredientDao ingredientDao;
    private final KitchenwareDao kitchenwareDao;

    @Override
    public void addRecipe(String name) {
        if (recipeDao.findRecipeByName(name).isEmpty()) {
            recipeDao.createRecipe(name);
        } else {
            log.info("Recipe with name " + name + " already exists");
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
