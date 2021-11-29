package com.netcracker.coctail.service.impl;

import com.netcracker.coctail.dao.IngredientDao;
import com.netcracker.coctail.exceptions.InvalidEmailOrPasswordException;
import com.netcracker.coctail.model.Ingredient;
import com.netcracker.coctail.service.IngredientService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.beans.PropertyEditorSupport;
import java.util.List;

@Service
@Slf4j
@Data
public class IngredientServiceImp implements IngredientService {

    private final IngredientDao ingredientDao;

    @Override
    public Ingredient getIngredientById(Long id){
        Ingredient result = ingredientDao.findIngredientById(id).get(0);
        if (result == null) {
            log.warn("IN getIngredientById - no ingredient found by id: {}", id);
            return null;
        }
        return result;
    }

    @Override
    public Ingredient getIngredientByName(String name){
        List<Ingredient> result = ingredientDao.findIngredientByName(name);
        if (result.isEmpty()) {
            throw new InvalidEmailOrPasswordException();
        }
        log.info("IN getIngredientByName - ingredient: {} found by name: {}", result.get(0).getName(), result.get(0).getId());
        return result.get(0);
    }

    @Override
    public void addIngredient(Ingredient ingredient){
        String result = ingredient.getName();
        if (ingredientDao.findIngredientByName(result).isEmpty()){
            ingredientDao.create(ingredient);
        }
        else {
            throw new InvalidEmailOrPasswordException();
            //type,category
        }
    }

    public void editIngredient (Ingredient ingredient){
        ingredientDao.editIngredient(ingredient);
    }
    public void removeIngredient(Ingredient ingredient){
        ingredientDao.removeIngredient(ingredient);
    }

}
