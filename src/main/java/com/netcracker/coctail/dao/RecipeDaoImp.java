package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@PropertySource("classpath:SQLscripts.properties")
public class RecipeDaoImp implements RecipeDao {

    @Value("${createRecipe}")
    private String createRecipe;
    @Value("${findRecipeByName}")
    private String findRecipeByName;
    @Value("${findAllRecipesByName}")
    private String findAllRecipesByName;
    @Value("${findRecipeById}")
    private String findRecipeById;
    @Value("${addIngredientToRecipe}")
    private String addIngredientToRecipe;
    @Value("${addKitchenwareToRecipe}")
    private String addKitchenwareToRecipe;
    @Value("${removeIngredientFromRecipe}")
    private String removeIngredientFromRecipe;
    @Value("${removeKitchenwareFromRecipe}")
    private String removeKitchenwareFromRecipe;
    @Value("${editRecipe}")
    private String editRecipe;
    @Value("${removeRecipe}")
    private String removeRecipe;
    @Value("${addToFavourites}")
    private String addToFavourites;
    @Value("${likeRecipe}")
    private String likeRecipe;
    @Value("${checkLike}")
    private String checkLike;
    @Value("${withdrawLike}")
    private String withdrawLike;
    @Value("${likedLock}")
    private String likedLock;
    @Value("${containsKitchenware}")
    private String containsKitchenware;
    @Value("${containsIngredients}")
    private String containsIngredients;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    RowMapper<Recipe> rowMapper = (rs, rownum) ->
            new Recipe(rs.getInt("id"),
                    rs.getString("recipe"),
                    rs.getInt("rating"),
                    rs.getBoolean("alcohol"),
                    rs.getBoolean("sugarless"),
                    rs.getBoolean("isActive"));

    @Override
    public List<Recipe> findRecipeByName(String name) {
        return jdbcTemplate.query(String.format(findRecipeByName, name), rowMapper);
    }

    @Override
    public List<Recipe> findAllRecipesByName(String name) {
        return jdbcTemplate.query(String.format(findAllRecipesByName, name + "%"), rowMapper);
    }

    @Override
    public void editRecipe(Recipe recipe) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", recipe.getId())
                .addValue("recipe", recipe.getName())
                .addValue("alcohol", recipe.isAlcohol())
                .addValue("sugarless", recipe.isSugarless())
                .addValue("isActive", recipe.isActive());
        jdbcTemplate.update(editRecipe, param);
    }

    @Override
    public void removeRecipe(int id) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id);
        jdbcTemplate.update(removeRecipe, param);
    }

    @Override
    public void addToFavourites(long userId, int recipeId) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("recipeid", recipeId)
                .addValue("userid", userId);
        jdbcTemplate.update(addToFavourites, param);
    }

    @Override
    public void likeRecipe(int recipeId) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", recipeId);
        jdbcTemplate.update(likeRecipe, param);
    }

    public void likedLock(long userId, int recipeId, boolean liked){
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("recipeid", recipeId)
                .addValue("userid", userId)
                .addValue("liked", liked);
        jdbcTemplate.update(likedLock, param);
    }

    @Override
    public List<UserToRecipe> checkLike(long userId, int recipeId) {
        RowMapper<UserToRecipe> rowMapper = (rs, rownum) ->
                new UserToRecipe(
                        rs.getInt("id"),
                        rs.getInt("userid"),
                        rs.getInt("recipeid"),
                        rs.getBoolean("liked"));
        return jdbcTemplate.query(String.format(checkLike, userId, recipeId), rowMapper);
    }

    @Override
    public void withdrawLike(int recipeId) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", recipeId);
        jdbcTemplate.update(withdrawLike, param);
    }

    @Override
    public List<Ingredient> containsIngredients(int recipeId) {
        RowMapper<Ingredient> rowMapper = (rs, rownum) ->
                new Ingredient(rs.getLong("id"),
                        rs.getString("ingredientsname"),
                        rs.getString("type"),
                        rs.getString("category"),
                        rs.getBoolean("isActive"));
        return jdbcTemplate.query(String.format(containsIngredients, recipeId), rowMapper);
    }

    @Override
    public List<Kitchenware> containsKitchenware(int recipeId) {
        RowMapper<Kitchenware> rowMapper = (rs, rownum) ->
                new Kitchenware(rs.getLong("id"),
                        rs.getString("kitchenwarename"),
                        rs.getString("type"),
                        rs.getString("category"),
                        rs.getBoolean("isActive"));
        return jdbcTemplate.query(String.format(containsKitchenware, recipeId), rowMapper);
    }

    @Override
    public void createRecipe(CreateRecipe recipe) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("recipe", recipe.getName())
                .addValue("rating", 0)
                .addValue("alcohol", recipe.isAlcohol())
                .addValue("sugarless", recipe.isSugarless())
                .addValue("isActive", recipe.isActive());
        jdbcTemplate.update(createRecipe, param);
    }

    @Override
    public List<Recipe> findRecipeById(Integer id) {
        return jdbcTemplate.query(String.format(findRecipeById, id), rowMapper);
    }

    @Override
    public void addIngredientToRecipe(Integer recipeId, Long ingredientId) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("recipeid", recipeId)
                .addValue("ingredientid", ingredientId);
        jdbcTemplate.update(addIngredientToRecipe, param);
    }

    @Override
    public void addKitchenwareToRecipe(Integer recipeId, Long kitchenwareId) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("recipeid", recipeId)
                .addValue("kitchenwareid", kitchenwareId);
        jdbcTemplate.update(addKitchenwareToRecipe, param);
    }

    @Override
    public void removeIngredientFromRecipe(Integer recipeId, Long ingredientId) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("recipeid", recipeId)
                .addValue("ingredientid", ingredientId);
        jdbcTemplate.update(removeIngredientFromRecipe, param);
    }

    @Override
    public void removeKitchenwareFromRecipe(Integer recipeId, Long kitchenwareId) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("recipeid", recipeId)
                .addValue("kitchenwareid", kitchenwareId);
        jdbcTemplate.update(removeKitchenwareFromRecipe, param);
    }

}
