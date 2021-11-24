package com.netcracker.coctail.controllers;


import com.netcracker.coctail.dao.IngredientDao;
import com.netcracker.coctail.dao.IngredientDaoImp;
import com.netcracker.coctail.dao.ModeratorDao;
import com.netcracker.coctail.model.Ingredient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping(value ="/api/ingredients/")
public class IngredientRestController {

    private final IngredientDao createIngredientDao;

    @GetMapping (value = "/{ingredientid}/"){

    }
    @PostMapping(value = "/create/"){

    }
    @PutMapping(value = "/edit/")

    void create(Ingredient ingredient )
        Collection<Ingredient> getAll() {
            return IngredientDao.getAll
        }
    @DeleteMapping(value = "{ingredientid}"){

    }

}
