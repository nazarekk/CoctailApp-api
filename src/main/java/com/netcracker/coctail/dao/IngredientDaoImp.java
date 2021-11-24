package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import java.util.Collection;
import java.util.List;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;


@Data
@Component
@PropertySource("classpath:SQLscripts.properties")
public class IngredientDaoImp implements IngredientDao {


    @Value("${findIngredientByName}")
    private String findIngredientByName;
    @Value("${findIngredientById}")
    private String findIngredientById;
    @Value  ("SELECT Ingredientid, name, type, category, isactive FROM ingredients WHERE isactive = '%s';")
    private String filterIngredient;
    @Value  ("INSERT INTO ingredients (id, name,type,category, isactive) values (:id, :nickname,:type,:category,:isactive")
    private String createIngredient;
    @Value  ("UPDATE ingredients set name = :name, isactive = :isactive WHERE userid = :userid")
    private String editIngredient;
    @Value  ("SELECT ingredientid, name, type, category, isactive FROM ingredients")
    private String getIngredients;
    @Value  ("UPDATE ingredients set  isactive = FALSE WHERE userid = :userid")


    private final JdbcTemplate jdbcTemplate;



    RowMapper<Ingredient> rowMapper = (rs, rownum) ->
            new Ingredient(rs.getLong("ingredientid"),
                    rs.getString("name"),
                    rs.getString("type"),
                    rs.getString("category"),
                    rs.getBoolean("isactive"));

    @Override
    public int create(Ingredient ingredient) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("ingredientid", ingredient.getId())
                .addValue("name", ingredient.getName())
                .addValue("type", ingredient.getType())
                .addValue("category", ingredient.getCategory());
                //.addValue("isactive", ingredient.getisactive);

        return jdbcTemplate.update(createIngredient, param);
    }

    public Collection<Ingredient> IngredientList() {
        RowMapper<Ingredient> rowMapper = (rs, rowNum) ->
                new Ingredient(
                        rs.getLong("ingredientid"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("category"),
                        rs.getBoolean("isactive"));
        return jdbcTemplate.query(getIngredients, rowMapper);
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
                .addValue("ingredientid", ingredient.getId())
                .addValue("name", ingredient.getName())
                .addValue("type", ingredient.getType())
                .addValue("category", ingredient.getCategory());
                //.addValue("isactive", ingredient.getIsActive());
        jdbcTemplate.update(editIngredient, param);
    }

    @Override
    public void removeIngredient(Ingredient ingredient) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("ingredientid", ingredient.getId());
        //jdbcTemplate.update(ingredient.IsActive(), param);
    }


    public Ingredient filterIngredients(Boolean IsActive) {
        RowMapper<Ingredient> rowMapper = (rs, rowNum) ->
                new Ingredient(
                        rs.getLong("userid"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("category"),
                        rs.getBoolean("isactive"));
        return jdbcTemplate.query(String.format(filterIngredient, IsActive), rowMapper).get(0);
    }


}

