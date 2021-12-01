package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.CreateIngredient;
import com.netcracker.coctail.model.Ingredient;

import java.util.List;


public interface IngredientDao {

    void create(CreateIngredient ingredient);

    List<Ingredient> findIngredientByName(String name);

    List<Ingredient> findIngredientById(Long id);

    void editIngredient(Ingredient ingredient);

    void removeIngredient(Ingredient ingredient);
}