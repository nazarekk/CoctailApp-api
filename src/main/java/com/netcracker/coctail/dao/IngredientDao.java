package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;


public interface IngredientDao {
    int create(Ingredient ingredient);

    List<Ingredient> findIngredientByName(String name);

    List<Ingredient> findIngredientById(Long id);

    void editIngredient(Ingredient ingredient);

    void removeIngredient(Ingredient ingredient);

    Ingredient filterIngredients(Boolean isactive);
}