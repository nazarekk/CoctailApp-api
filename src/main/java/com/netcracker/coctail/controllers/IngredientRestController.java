package com.netcracker.coctail.controllers;


import com.netcracker.coctail.dao.IngredientDao;
import com.netcracker.coctail.dto.IngredientDto;
import com.netcracker.coctail.model.Ingredient;
import com.netcracker.coctail.service.IngredientService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collection;

@RestController
public class IngredientRestController {
    @Resource
    IngredientDao ingredientDao;
    IngredientService ingredientService;
    Ingredient ingredient;

    @GetMapping("/ingredients")

    public Collection<Ingredient> ingredientList() {
        return ingredientDao.getIngredients();
    }
    @GetMapping (value = "/api/ingredients/{id}")
    public IngredientDto getIngredientById(@PathVariable(name = "id")long id){
        Ingredient ingredient = ingredientService.getIngredientById(id);
        IngredientDto result = IngredientDto.fromIngredient(ingredient);
        return result;
    }

    @GetMapping (value = "/api/ingredients/{name}")
    public IngredientDto getIngredientByName(@PathVariable(name = "name")String name){
        Ingredient ingredient = ingredientService.getIngredientByName(name);
        IngredientDto result = IngredientDto.fromIngredient(ingredient);
        return result;
    }

    @PostMapping(value = "/api/ingredients/create")
    public void addIngredient(@RequestBody @Valid Ingredient ingredient) {
        ingredientService.addIngredient(ingredient);
    }

    @PatchMapping(value = "/edit/")
    void update(Ingredient ingredient){
        ingredientService.editIngredient(ingredient);
        }

    @DeleteMapping(value = "/api/ingredients/remove")
    public void removeIngredient(Ingredient ingredient){
        ingredientService.removeIngredient(ingredient);
    }

}
