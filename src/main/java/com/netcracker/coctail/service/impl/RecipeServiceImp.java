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
    name = name.replaceAll("[^A-Za-z0-9]", "");
    List<Recipe> recipes = recipeDao.findAllRecipesByName(name);
    UserToRecipe userToRecipe;
    Boolean liked = null;
    Boolean favourite = null;
    String token = httpServletRequest.getHeader("Authorization");
    List<DishRecipe> result = new ArrayList<>();
    long userid = 0;
    if (token != null) {
      userid =
          userDao.findUserByEmail(jwtTokenProvider.getEmail(token.substring(7))).get(0).getId();
    }
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
  public List<DishRecipe> getRecipesFiltered(String sugarless, String alcohol,
                                             HttpServletRequest httpServletRequest) {
    log.info("Filtering");
    List<Recipe> recipes = recipeDao.findAllRecipesFiltered(sugarless, alcohol);
    UserToRecipe userToRecipe;
    Boolean liked = null;
    Boolean favourite = null;
    String token = httpServletRequest.getHeader("Authorization");
    List<DishRecipe> result = new ArrayList<>();
    long userid = 0;
    if (token != null) {
      userid =
          userDao.findUserByEmail(jwtTokenProvider.getEmail(token.substring(7))).get(0).getId();
    }
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
            log.error("Recipe with id {} doesn't exist", id);
            return false;
        }
        if (recipeDao.findRecipeByName(name).isEmpty() | result.getName().equals(name)) {
            recipeDao.editRecipe(recipe);
            return true;
        } else {
            log.error("Recipe with name {} already exists", name);
            return false;
        }
    }

    @Override
    public boolean removeRecipe(int id) {
        Recipe result = recipeDao.findRecipeById(id).get(0);
        if (result == null) {
            log.error("Recipe with id {} doesn't exist", id);
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
            log.error("You already added recipe with id {} to favourites", recipeId);
            return false;
        } else if (!recipeDao.checkLike(ownerId, recipeId).get(0).isFavourite()) {
            log.error("You didn't add recipe with id {} to favourites", recipeId);
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
        }
        else if (like) {
            log.error("You already liked this recipe");
            return false;
        } else if (!recipeDao.checkLike(ownerId, recipeId).get(0).isLiked()) {
            log.error("You didn't like this recipe");
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
            log.error("Recipe with name {} already exists", recipe.getName());
            return 0;
        }
    }

    @Override
    public boolean addIngredientToRecipe(int recipeId, String name) {
        Recipe recipe = recipeDao.findRecipeById(recipeId).get(0);
        Ingredient ingredient = ingredientDao.findIngredientByName(name).get(0);
        if (recipe == null) {
            log.error("Recipe with id {} doesn't exist", recipeId);
            return false;
        }
        if (ingredient == null) {
            log.error("Ingredient with name {} doesn't exist", name);
            return false;
        }
        if (!recipeDao.ingredientInRecipe(recipeId, ingredient.getId())) {
            recipeDao.addIngredientToRecipe(recipe.getId(), ingredient.getId());
            return true;
        } else {
            log.error("Ingredient with name {} is already included", name);
            return false;
        }
    }

    @Override
    public boolean addKitchenwareToRecipe(int recipeId, String name) {
        Recipe recipe = recipeDao.findRecipeById(recipeId).get(0);
        Kitchenware kitchenware = kitchenwareDao.findKitchenwareByName(name).get(0);
        if (recipe == null) {
            log.error("Recipe with id {} doesn't exist", recipeId);
            return false;
        }
        if (kitchenware == null) {
            log.error("Kitchenware with name {} doesn't exist", name);
            return false;
        }
        if (!recipeDao.kitchenwareInRecipe(recipeId, kitchenware.getId())) {
            recipeDao.addKitchenwareToRecipe(recipe.getId(), kitchenware.getId());
            return true;
        } else {
            log.error("Kitchenware with with name {} is already included", name);
            return false;
        }
    }

    @Override
    public boolean removeIngredientFromRecipe(int recipeId, String name) {
        Recipe recipe = recipeDao.findRecipeById(recipeId).get(0);
        Ingredient ingredient = ingredientDao.findIngredientByName(name).get(0);
        if (recipe == null) {
            log.error("Recipe with id {} doesn't exist", recipeId);
            return false;
        }
        if (ingredient == null) {
            log.error("Ingredient with name {} doesn't exist", name);
            return false;
        }
        if (recipeDao.ingredientInRecipe(recipeId, ingredient.getId())) {
            recipeDao.removeIngredientFromRecipe(recipe.getId(), ingredient.getId());
            return true;
        } else {
            log.error("Ingredient with name {} is not included", name);
            return false;
        }
    }

    @Override
    public boolean removeKitchenwareFromRecipe(int recipeId, String name) {
        Recipe recipe = recipeDao.findRecipeById(recipeId).get(0);
        Kitchenware kitchenware = kitchenwareDao.findKitchenwareByName(name).get(0);
        if (recipe == null) {
            log.error("Recipe with id {} doesn't exist", recipeId);
            return false;
        }
        if (kitchenware == null) {
            log.error("Kitchenware with name {} doesn't exist", name);
            return false;
        }
        if (recipeDao.kitchenwareInRecipe(recipeId, kitchenware.getId())) {
            recipeDao.removeKitchenwareFromRecipe(recipe.getId(), kitchenware.getId());
            return true;
        } else {
            log.error("Kitchenware with name {} is not included", name);
            return false;
        }
    }

  @Override
  public List<DishRecipe> getSuggestion(String header) {
    Long userid =
        userDao.findUserByEmail(jwtTokenProvider.getEmail(header.substring(7))).get(0).getId();
    List<Recipe> recipes = recipeDao.getSuggestion(userid);
    UserToRecipe userToRecipe;
    Boolean liked = null;
    Boolean favourite = null;
    List<DishRecipe> result = new ArrayList<>();
    for (Recipe recipe : recipes) {
      if (!recipeDao.checkLike(userid, recipe.getId()).isEmpty()) {
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
}
