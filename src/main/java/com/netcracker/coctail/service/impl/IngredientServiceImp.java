package com.netcracker.coctail.service.impl;

import com.netcracker.coctail.dao.IngredientDao;
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
    List<Ingredient> result = ingredientDao.findAllIngredientByName(name);
    return result;
  }

  @Override
  public List<Ingredient> getIngredientFiltered(String type, String category, String active) {
    List<Ingredient> result = ingredientDao.findAllIngredientFiltered(type, category, active);
    return result;
  }

  @Override
  public Boolean addIngredient(CreateIngredient ingredient) {
    String name = ingredient.getName();
    if (ingredientDao.findIngredientByName(name).isEmpty()) {
      ingredientDao.create(ingredient);
      log.info("Ingredient with name {} created", name);
      return Boolean.TRUE;
    } else {
      log.error("Ingredient with name {} already exists", name);
      return Boolean.FALSE;
    }
  }

  @Override
  public Boolean editIngredient(Ingredient ingredient) {
    String name = ingredient.getName();
    long id = ingredient.getId();
    Ingredient result = ingredientDao.findIngredientById(id).get(0);
    if (result == null) {
      log.error("Ingredient with id {} doesn't exists", id);
      return Boolean.FALSE;
    }
    if (ingredientDao.findIngredientByName(name).isEmpty()) {
      ingredientDao.editIngredient(ingredient);
      return Boolean.TRUE;
    } else if (result.getName().equals(name)) {
      ingredientDao.editIngredient(ingredient);
      return Boolean.TRUE;
    } else {
      log.error("Ingredient with name " + name + " already exists but in db " + result.getName());
      return Boolean.FALSE;
    }
  }


  @Override
  public Boolean removeIngredient(long id) {
    Ingredient result = ingredientDao.findIngredientById(id).get(0);
    if (result == null) {
      log.error("Ingredient with id {} doesn't exist", id);
      return Boolean.FALSE;
    }
    ingredientDao.removeIngredient(result);
    return Boolean.TRUE;
  }


}