package com.netcracker.coctail.service.impl;

import com.netcracker.coctail.dao.IngredientDao;
import com.netcracker.coctail.exceptions.InvalidEmailOrPasswordException;
import com.netcracker.coctail.model.CreateIngredient;
import com.netcracker.coctail.model.Ingredient;
import com.netcracker.coctail.service.IngredientService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Data
public class IngredientServiceImp implements IngredientService {

    private final IngredientDao ingredientDao;

    @Override
    public Ingredient getIngredientById(Long id) {
        Ingredient result = ingredientDao.findIngredientById(id).get(0);
        if (result == null) {
            log.warn("IN getIngredientById - no ingredient found by id: {}", id);
            return null;
        }
        return result;
    }

    @Override
    public List<Ingredient> getIngredientByName(String name) {
        List<Ingredient> result = ingredientDao.findIngredientByName(name);
        if (result.isEmpty()) {
            throw new InvalidEmailOrPasswordException();
        }
        return result;
    }

    @Override
    public void addIngredient(CreateIngredient ingredient) {
        String name = ingredient.getName();
        if (ingredientDao.findIngredientByName(name).isEmpty()) {
            ingredientDao.create(ingredient);
        } else {
            log.info("Ingredient with name " + name + " already exists");
            throw new InvalidEmailOrPasswordException();
        }
    }

    @Override
    public void editIngredient(Ingredient ingredient) {
        log.info("Ingredient isActive = " + ingredient.isActive());
        long id = ingredient.getId();
        Ingredient result = ingredientDao.findIngredientById(id).get(0);
        if (result == null) {
            log.info("Ingredient with id " + id + " doesn't exists");
            throw new InvalidEmailOrPasswordException();
        }
        if (ingredient.getName().equals("")) {
            ingredient.setName(result.getName());
        }
        if (ingredient.getType().equals("")) {
            ingredient.setType(result.getType());
        }
        if (ingredient.getCategory().equals("")) {
            ingredient.setCategory(result.getCategory());
        }
        log.info("Ingredient isActive = " + ingredient.isActive());
        ingredientDao.editIngredient(ingredient);
    }

    @Override
    public void removeIngredient(long id) {
        Ingredient result = ingredientDao.findIngredientById(id).get(0);
        if (result == null) {
            log.info("Ingredient with id " + id + " doesn't exists");
            throw new InvalidEmailOrPasswordException();
        }
        ingredientDao.removeIngredient(result);
    }


}
