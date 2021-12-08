package com.netcracker.coctail.controllers;

import com.netcracker.coctail.dao.ModeratorDao;
import com.netcracker.coctail.model.ActivateModerator;
import com.netcracker.coctail.model.CreateIngredient;
import com.netcracker.coctail.model.CreateKitchenware;
import com.netcracker.coctail.model.Ingredient;
import com.netcracker.coctail.model.Kitchenware;
import com.netcracker.coctail.model.Recipe;
import com.netcracker.coctail.model.CreateRecipe;
import com.netcracker.coctail.service.IngredientService;
import com.netcracker.coctail.service.KitchenwareService;
import com.netcracker.coctail.service.RecipeService;
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
    private final RecipeService recipeService;

  @PostMapping("activation")
  public String activateModerator(@RequestBody ActivateModerator moderator) {
    createModeratorDao.activateModerator(moderator);
    return "Account is activated!";
  }

  @GetMapping("ingredients")
  public ResponseEntity<List<Ingredient>> getIngredientsByName(@RequestParam String name) {
    List<Ingredient> ingredients = ingredientService.getIngredientByName(name);
    if (ingredients.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(ingredients, HttpStatus.OK);
  }

  @GetMapping("ingredients/filter")
  public ResponseEntity<List<Ingredient>> getIngredientsFiltered(
      @RequestParam String type,
      @RequestParam String category,
      @RequestParam String active) {
    List<Ingredient> ingredients = ingredientService.getIngredientFiltered(type, category, active);
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
  public ResponseEntity addIngredient(@RequestBody CreateIngredient ingredient) {
    Boolean ret = ingredientService.addIngredient(ingredient);
    return ret == Boolean.TRUE
        ? new ResponseEntity(ret, HttpStatus.OK) :
        new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
  }

  @PutMapping(value = "ingredients")
  public ResponseEntity editIngredient(@RequestBody Ingredient ingredient) {
    Boolean ret = ingredientService.editIngredient(ingredient);
    return ret == Boolean.TRUE
        ? new ResponseEntity(ret, HttpStatus.OK) :
        new ResponseEntity(HttpStatus.NOT_MODIFIED);
  }

  @DeleteMapping(value = "ingredients/{id}")
  public ResponseEntity removeIngredient(@PathVariable(name = "id") long id) {
    return ingredientService.removeIngredient(id) == Boolean.TRUE
        ? new ResponseEntity(ingredientService.removeIngredient(id), HttpStatus.OK) :
        new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
  }

  @GetMapping("kitchenware")
  public ResponseEntity<List<Kitchenware>> getKitchenwareByName(@RequestParam String name) {
    List<Kitchenware> kitchenware = kitchenwareService.getKitchenwareByName(name);
    if (kitchenware.isEmpty()) {
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>(kitchenware, HttpStatus.OK);
  }

  @GetMapping("kitchenware/filter")
  public ResponseEntity<List<Kitchenware>> getKitchenwareFiltered(
      @RequestParam String type,
      @RequestParam String category,
      @RequestParam String active) {
    List<Kitchenware> kitchenware = kitchenwareService.getKitchenwareFiltered(type, category, active);
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
  public ResponseEntity addKitchenware(@RequestBody CreateKitchenware kitchenware) {
    Boolean ret = kitchenwareService.addKitchenware(kitchenware);
    return ret == Boolean.TRUE
        ? new ResponseEntity(ret, HttpStatus.OK) :
        new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
  }

  @PutMapping(value = "kitchenware")
  public ResponseEntity editKitchenware(@RequestBody Kitchenware kitchenware) {
    Boolean ret = kitchenwareService.editKitchenware(kitchenware);
    return ret == Boolean.TRUE
        ? new ResponseEntity(ret, HttpStatus.OK) :
        new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
  }

  @DeleteMapping(value = "kitchenware/{id}")
  public ResponseEntity removeKitchenware(@PathVariable(name = "id") long id) {
    return kitchenwareService.removeKitchenware(id) == Boolean.TRUE
        ? new ResponseEntity(kitchenwareService.removeKitchenware(id), HttpStatus.OK) :
        new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
  }

    @PostMapping(value = "recipe")
    public ResponseEntity<Integer> addRecipe(@RequestBody CreateRecipe recipe) {
        int id = recipeService.addRecipe(recipe);
        if (id > 0) {
            return new ResponseEntity<>(id, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping(value = "recipe/{id}/ingredients")
    public ResponseEntity addIngredientToRecipe(@PathVariable(name = "id") int id, @RequestParam String name) {
        if (recipeService.addIngredientToRecipe(id, name)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping(value = "recipe/{id}/kitchenware")
    public ResponseEntity addKitchenwareToRecipe(@PathVariable(name = "id") int id, @RequestParam String name) {
        if (recipeService.addKitchenwareToRecipe(id, name)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @DeleteMapping(value = "recipe/{id}/ingredients")
    public ResponseEntity removeIngredientFromRecipe(@PathVariable(name = "id") int id, @RequestParam String name) {
        if (recipeService.removeIngredientFromRecipe(id, name)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @DeleteMapping(value = "recipe/{id}/kitchenware")
    public ResponseEntity removeKitchenwareFromRecipe(@PathVariable(name = "id") int id, @RequestParam String name) {
        if (recipeService.removeKitchenwareFromRecipe(id, name)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PutMapping(value = "recipe")
    public ResponseEntity editRecipe(@RequestBody Recipe recipe) {
        if (recipeService.editRecipe(recipe)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @DeleteMapping(value = "recipe/{id}")
    public ResponseEntity removeRecipe(@PathVariable(name = "id") int id) {
        if (recipeService.removeRecipe(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

}