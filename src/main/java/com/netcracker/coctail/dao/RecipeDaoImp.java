package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.Recipe;
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

    private final NamedParameterJdbcTemplate jdbcTemplate;

    RowMapper<Recipe> rowMapper = (rs, rownum) ->
            new Recipe(rs.getInt("id"),
                    rs.getString("recipe"),
                    rs.getInt("rating"));

    @Override
    public List<Recipe> findRecipeByName(String name) {
        return jdbcTemplate.query(String.format(findRecipeByName, name), rowMapper);
    }

    @Override
    public void createRecipe(String name) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("recipe", name)
                .addValue("rating", 0);
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
