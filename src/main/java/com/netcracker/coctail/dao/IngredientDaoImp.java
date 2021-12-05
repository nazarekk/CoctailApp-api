package com.netcracker.coctail.dao;

import java.util.List;

import com.netcracker.coctail.model.CreateIngredient;
import com.netcracker.coctail.model.Ingredient;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;


@Data
@Component
@PropertySource("classpath:SQLscripts.properties")
public class IngredientDaoImp implements IngredientDao {

    @Value("${findAllIngredientByName}")
    private String findAllIngredientByName;
    @Value("${findAllIngredientFiltered}")
    private String findAllIngredientFiltered;
    @Value("${findIngredientByName}")
    private String findIngredientByName;
    @Value("${findIngredientById}")
    private String findIngredientById;
    @Value("${createIngredient}")
    private String createIngredient;
    @Value("${editIngredient}")
    private String editIngredient;
    @Value("${removeIngredient}")
    private String removeIngredient;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    RowMapper<Ingredient> rowMapper = (rs, rownum) ->
            new Ingredient(rs.getLong("id"),
                    rs.getString("ingredientsname"),
                    rs.getString("type"),
                    rs.getString("category"),
                    rs.getBoolean("isActive"));

    @Override
    public void create(CreateIngredient ingredient) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("ingredientsname", ingredient.getName())
                .addValue("type", ingredient.getType())
                .addValue("category", ingredient.getCategory())
                .addValue("isActive", ingredient.isActive());
        jdbcTemplate.update(createIngredient, param);
    }

    @Override
    public List<Ingredient> findAllIngredientByName(String name) {
        return jdbcTemplate.query(String.format(findAllIngredientByName, name + "%"), rowMapper);
    }

    @Override
    public List<Ingredient> findAllIngredientFiltered(String type, String category) {
        return jdbcTemplate.query(String.format(findAllIngredientFiltered, type, category), rowMapper);
    }

    @Override
    public List<Ingredient> findIngredientByName(String name) {
        return jdbcTemplate.query(String.format(findIngredientByName, name), rowMapper);
    }

    @Override
    public List<Ingredient> findIngredientById(Long id) {
        return jdbcTemplate.query(String.format(findIngredientById, id), rowMapper);
    }

    @Override
    public void editIngredient(Ingredient ingredient) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", ingredient.getId())
                .addValue("ingredientsname", ingredient.getName())
                .addValue("type", ingredient.getType())
                .addValue("category", ingredient.getCategory())
                .addValue("isActive", ingredient.isActive());
        jdbcTemplate.update(editIngredient, param);
    }

    @Override
    public void removeIngredient(Ingredient ingredient) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", ingredient.getId());
        jdbcTemplate.update(removeIngredient, param);
    }

}

