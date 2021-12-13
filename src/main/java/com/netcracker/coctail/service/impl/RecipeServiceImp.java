package com.netcracker.coctail.service.impl;

import com.netcracker.coctail.dao.FriendlistDao;
import com.netcracker.coctail.dao.IngredientDao;
import com.netcracker.coctail.dao.KitchenwareDao;
import com.netcracker.coctail.dao.RecipeDao;
import com.netcracker.coctail.dao.UserDao;
import com.netcracker.coctail.model.CreateRecipe;
import com.netcracker.coctail.model.DishRecipe;
import com.netcracker.coctail.model.Ingredient;
import com.netcracker.coctail.model.Recipe;
import com.netcracker.coctail.model.Kitchenware;
import com.netcracker.coctail.model.UserToRecipe;
import com.netcracker.coctail.security.jwt.JwtTokenProvider;
import com.netcracker.coctail.service.RecipeService;
import javax.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@Data
public class RecipeServiceImp implements RecipeService {

  private RecipeDao recipeDao;
  private IngredientDao ingredientDao;
  private KitchenwareDao kitchenwareDao;
  private FriendlistDao friendlistDao;
  private JwtTokenProvider jwtTokenProvider;
  private UserDao userDao;

  @Autowired
  @Lazy
  public RecipeServiceImp(RecipeDao recipeDao, IngredientDao ingredientDao,
                          KitchenwareDao kitchenwareDao,
                          FriendlistDao friendlistDao,
                          JwtTokenProvider jwtTokenProvider,
                          UserDao userDao) {
    this.recipeDao = recipeDao;
    this.ingredientDao = ingredientDao;
    this.kitchenwareDao = kitchenwareDao;
    this.friendlistDao = friendlistDao;
    this.jwtTokenProvider = jwtTokenProvider;
    this.userDao = userDao;
  }

  @Override
  public List<DishRecipe> getRecipesByName(String name, HttpServletRequest httpServletRequest) {
    List<Recipe> recipes = recipeDao.findAllRecipesByName(name);
    UserToRecipe userToRecipe;
    Boolean liked = null;
    Boolean favourite = null;
    String token = httpServletRequest.getHeader("Authorization");
    List<UserToRecipe> userToRecipes;
    List<DishRecipe> result = new ArrayList<>();
    long userid = 0;
    if (token != null) {userid =
        userDao.findUserByEmail(jwtTokenProvider.getEmail(token.substring(7))).get(0).getId();}
    for (Recipe recipe : recipes) {
      if (token != null & !recipeDao.checkLike(userid, recipe.getId()).isEmpty()) {
        userToRecipe = recipeDao.checkLike(userid, recipe.getId()).get(0);
        liked = userToRecipe.isLiked();
        favourite = userToRecipe.isFavourite();
      }
      int recipeId = recipe.getId();
      result.add(new DishRecipe(
          recipe.getId(),
          recipe.getName(),
          recipe.getAlcohol(),
          recipe.isSugarless(),
          recipe.isActive(),
          recipe.getImage(),
          recipe.getRecipe(),
          recipe.getRating(),
          recipeDao.containsIngredients(recipeId),
          recipeDao.containsKitchenware(recipeId),
          liked,
          favourite
      ));
    }
    return result;
  }

  @Override
  public List<DishRecipe> getRecipesFiltered(boolean sugarless, String alcohol) {
    log.info("Filtering");
    List<Recipe> recipes = recipeDao.findAllRecipesFiltered(sugarless, alcohol);
    Boolean liked = null;
    Boolean favourite = null;
    String token = null;
    if (!token.isEmpty()) {
      Long userid =
          userDao.findUserByEmail(jwtTokenProvider.getEmail(token.substring(7))).get(0).getId();
      Integer recipeid = recipeDao.findRecipeByName("name").get(0).getId();
      UserToRecipe userToRecipe = recipeDao.checkLike(userid, recipeid).get(0);
      liked = userToRecipe.isLiked();
      favourite = userToRecipe.isFavourite();
    }
    List<DishRecipe> result = new ArrayList<>();
    for (Recipe recipe : recipes) {
      int recipeId = recipe.getId();
      result.add(new DishRecipe(
          recipe.getId(),
          recipe.getName(),
          recipe.getAlcohol(),
          recipe.isSugarless(),
          recipe.isActive(),
          recipe.getImage(),
          recipe.getRecipe(),
          recipe.getRating(),
          recipeDao.containsIngredients(recipeId),
          recipeDao.containsKitchenware(recipeId),
          liked,
          favourite
      ));
    }
    return result;
  }

  @Override
  public DishRecipe getRecipeById(int id) {
    Recipe recipe = recipeDao.findRecipeById(id).get(0);
    if (recipe == null) {
      log.warn("IN getRecipeById - no recipes found by id: {}", id);
      return null;
    }
    Boolean liked = null;
    Boolean favourite = null;
    return new DishRecipe(
        recipe.getId(),
        recipe.getName(),
        recipe.getAlcohol(),
        recipe.isSugarless(),
        recipe.isActive(),
        recipe.getImage(),
        recipe.getRecipe(),
        recipe.getRating(),
        recipeDao.containsIngredients(id),
        recipeDao.containsKitchenware(id),
        liked,
        favourite
    );
  }

  @Override
  public boolean editRecipe(Recipe recipe) {
    int id = recipe.getId();
    String name = recipe.getName();
    Recipe result = recipeDao.findRecipeById(id).get(0);
    if (result == null) {
      log.info("Recipe with id " + id + " doesn't exist");
      return false;
    }
    if (recipeDao.findRecipeByName(name).isEmpty() | recipe.getName().equals(result.getName())) {
      recipeDao.editRecipe(recipe);
      return true;
    } else {
      log.info("Recipe with name " + name + " already exists");
      return false;
    }
  }

  @Override
  public boolean removeRecipe(int id) {
    Recipe result = recipeDao.findRecipeById(id).get(0);
    if (result == null) {
      log.info("Recipe with id " + id + " doesn't exist");
      return false;
    }
    recipeDao.removeRecipe(result.getId());
    return true;
  }

  @Override
  public boolean addToFavourites(String ownerEmail, int recipeId, boolean favourite) {
    long ownerId = friendlistDao.getOwnerId(ownerEmail);
    if (recipeDao.checkLike(ownerId, recipeId).isEmpty()) {
      recipeDao.addToFavourites(ownerId, recipeId);
    }
    if (favourite && !recipeDao.checkLike(ownerId, recipeId).get(0).isFavourite()) {
      recipeDao.favouriteLock(ownerId, recipeId, favourite);
      return true;
    } else if (favourite) {
      log.info("You already added this recipe to favourites");
      return false;
    } else if (!recipeDao.checkLike(ownerId, recipeId).get(0).isFavourite()) {
      log.info("You didn't add this recipe to favourites");
      return false;
    } else {
      recipeDao.favouriteLock(ownerId, recipeId, favourite);
      return true;
    }

  }

  @Override
  public boolean likeRecipe(String ownerEmail, int recipeId, boolean like) {
    long ownerId = friendlistDao.getOwnerId(ownerEmail);
    if (recipeDao.checkLike(ownerId, recipeId).isEmpty()) {
      recipeDao.addToFavourites(ownerId, recipeId);
    }
    if (like && !recipeDao.checkLike(ownerId, recipeId).get(0).isLiked()) {
      recipeDao.likeRecipe(recipeId);
      recipeDao.likedLock(ownerId, recipeId, like);
      return true;
    } else if (like) {
      log.info("You already liked this recipe");
      return false;
    } else if (!recipeDao.checkLike(ownerId, recipeId).get(0).isLiked()) {
      log.info("You didn't like this recipe");
      return false;
    } else {
      recipeDao.withdrawLike(recipeId);
      recipeDao.likedLock(ownerId, recipeId, like);
      return true;
    }
  }

  @Override
  public Integer addRecipe(CreateRecipe recipe) {

    if (recipeDao.findRecipeByName(recipe.getName()).isEmpty()) {
      recipeDao.createRecipe(recipe);
      return recipeDao.findRecipeByName(recipe.getName()).get(0).getId();
    } else {
      log.info("Recipe with name " + recipe.getName() + " already exists");
      return 0;
    }
  }

  @Override
  public boolean addIngredientToRecipe(int recipeId, String name) {
    Recipe recipe = recipeDao.findRecipeById(recipeId).get(0);
    Ingredient ingredient = ingredientDao.findIngredientByName(name).get(0);
    if (recipe == null) {
      log.info("Recipe with id " + recipeId + " doesn't exist");
      return false;
    }
    if (ingredient == null) {
      log.info("Ingredient with name " + name + " doesn't exist");
      return false;
    }
    if (!recipeDao.ingredientInRecipe(recipeId, ingredient.getId())) {
      recipeDao.addIngredientToRecipe(recipe.getId(), ingredient.getId());
      return true;
    } else {
      log.info("Ingredient with name " + name + " is already included");
      return false;
    }
  }

  @Override
  public boolean addKitchenwareToRecipe(int recipeId, String name) {
    Recipe recipe = recipeDao.findRecipeById(recipeId).get(0);
    Kitchenware kitchenware = kitchenwareDao.findKitchenwareByName(name).get(0);
    if (recipe == null) {
      log.info("Recipe with id " + recipeId + " doesn't exist");
      return false;
    }
    if (kitchenware == null) {
      log.info("Kitchenware with name " + name + " doesn't exist");
      return false;
    }
    if (!recipeDao.kitchenwareInRecipe(recipeId, kitchenware.getId())) {
      recipeDao.addKitchenwareToRecipe(recipe.getId(), kitchenware.getId());
      return true;
    } else {
      log.info("Kitchenware with name " + name + " is already included");
      return false;
    }
  }

  @Override
  public boolean removeIngredientFromRecipe(int recipeId, String name) {
    Recipe recipe = recipeDao.findRecipeById(recipeId).get(0);
    Ingredient ingredient = ingredientDao.findIngredientByName(name).get(0);
    if (recipe == null) {
      log.info("Recipe with id " + recipeId + " doesn't exist");
      return false;
    }
    if (ingredient == null) {
      log.info("Ingredient with name " + name + " doesn't exist");
      return false;
    }
    if (recipeDao.ingredientInRecipe(recipeId, ingredient.getId())) {
      recipeDao.removeIngredientFromRecipe(recipe.getId(), ingredient.getId());
      return true;
    } else {
      log.info("Ingredient with name " + name + " is not included");
      return false;
    }
  }

  @Override
  public boolean removeKitchenwareFromRecipe(int recipeId, String name) {
    Recipe recipe = recipeDao.findRecipeById(recipeId).get(0);
    Kitchenware kitchenware = kitchenwareDao.findKitchenwareByName(name).get(0);
    if (recipe == null) {
      log.info("Recipe with id " + recipeId + " doesn't exist");
      return false;
    }
    if (kitchenware == null) {
      log.info("Kitchenware with name " + name + " doesn't exist");
      return false;
    }
    if (recipeDao.kitchenwareInRecipe(recipeId, kitchenware.getId())) {
      recipeDao.removeKitchenwareFromRecipe(recipe.getId(), kitchenware.getId());
      return true;
    } else {
      log.info("Kitchenware with name " + name + " is not included");
      return false;
    }
  }
}
