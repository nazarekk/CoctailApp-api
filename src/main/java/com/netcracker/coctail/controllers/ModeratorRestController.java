package com.netcracker.coctail.controllers;

import com.netcracker.coctail.dao.ModeratorDao;
import com.netcracker.coctail.model.ActivateModerator;
import com.netcracker.coctail.model.CreateIngredient;
import com.netcracker.coctail.model.CreateKitchenware;
import com.netcracker.coctail.model.Ingredient;
import com.netcracker.coctail.model.Kitchenware;
import com.netcracker.coctail.service.IngredientService;
import com.netcracker.coctail.service.KitchenwareService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/moderator/")
@CrossOrigin(origins = "*")
@Data
public class ModeratorRestController {

    private final ModeratorDao createModeratorDao;

    private final IngredientService ingredientService;
    private final KitchenwareService kitchenwareService;


    @PostMapping("activation")
    public String activateModerator(@RequestBody ActivateModerator moderator) {
        createModeratorDao.activateModerator(moderator);
        return "Account is activated!";
    }

    @GetMapping("ingredients/search")
    public ResponseEntity<List<Ingredient>> getIngredientsByName(@RequestParam String name) {
        List<Ingredient> ingredients = ingredientService.getIngredientByName(name);
        if (ingredients.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(ingredients, HttpStatus.OK);
    }

    @GetMapping("ingredients/list")
    public ResponseEntity<List<Ingredient>> ingredientsList() {
        List<Ingredient> ingredients = ingredientService.getIngredientByName("");
        if (ingredients.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(ingredients, HttpStatus.OK);
    }

    @GetMapping(value = "ingredients/{id}")
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable(name = "id") long id) {
        Ingredient result = ingredientService.getIngredientById(id);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "ingredients")
    public void addIngredient(@RequestBody CreateIngredient ingredient) {
        ingredientService.addIngredient(ingredient);
    }

    @PatchMapping(value = "ingredients/edit")
    void editIngredient(@RequestBody Ingredient ingredient) {
        ingredientService.editIngredient(ingredient);
    }

    @DeleteMapping(value = "ingredients/{id}")
    public void removeIngredient(@PathVariable(name = "id") long id) {
        ingredientService.removeIngredient(id);
    }

    @GetMapping("kitchenware/search")
    public ResponseEntity<List<Kitchenware>> getKitchenwareByName(@RequestParam String name) {
        List<Kitchenware> kitchenware = kitchenwareService.getKitchenwareByName(name);
        if (kitchenware.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(kitchenware, HttpStatus.OK);
    }

    @GetMapping("kitchenware/list")
    public ResponseEntity<List<Kitchenware>> kitchenwareList() {
        List<Kitchenware> kitchenware = kitchenwareService.getKitchenwareByName("");
        if (kitchenware.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(kitchenware, HttpStatus.OK);
    }

    @GetMapping(value = "kitchenware/{id}")
    public ResponseEntity<Kitchenware> getKitchenwareById(@PathVariable(name = "id") long id) {
        Kitchenware result = kitchenwareService.getKitchenwareById(id);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "kitchenware")
    public void addKitchenware(@RequestBody CreateKitchenware kitchenware) {
        kitchenwareService.addKitchenware(kitchenware);
    }

    @PatchMapping(value = "kitchenware/edit")
    void editKitchenware(@RequestBody Kitchenware kitchenware) {
        kitchenwareService.editKitchenware(kitchenware);
    }

    @DeleteMapping(value = "kitchenware/{id}")
    public void removeKitchenware(@PathVariable(name = "id") long id) {
        kitchenwareService.removeKitchenware(id);
    }

}