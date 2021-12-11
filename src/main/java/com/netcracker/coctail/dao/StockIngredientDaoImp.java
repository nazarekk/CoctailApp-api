package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.StockIngredientInfo;
import com.netcracker.coctail.model.StockIngredient;
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
public class StockIngredientDaoImp implements StockIngredientDao {

  @Value("${addStockIngredient}")
  private String addStockIngredient;
  @Value("{getUserIdByEmail}")
  private String getUserIdByEmail;
  @Value("${findExistingStockIngredientById}")
  private String findExistingStockIngredientById;
  @Value("${removeStockIngredient}")
  private String removeStockIngredient;
  @Value("${editStockIngredient}")
  private String editStockIngredient;
  @Value("${findStockIngredientsByName}")
  private String findStockIngredientsByName;
  @Value("${findStockIngredientsFiltered}")
  private String findStockIngredientsFiltered;

  private final NamedParameterJdbcTemplate jdbcTemplate;

  @Override
  public void addIngredientToStock(long ownerId, long ingredientId, long quantity) {
    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("userid", ownerId)
        .addValue("ingredientid", ingredientId)
        .addValue("quantity", quantity);
    jdbcTemplate.update(addStockIngredient, param);
  }

  @Override
  public void editStockIngredient(long id, long quantity) {
    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("id", id)
        .addValue("quantity", quantity);
    jdbcTemplate.update(editStockIngredient, param);
  }

  @Override
  public void removeIngredientFromStock(long id) {
    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("id", id);
    jdbcTemplate.update(removeStockIngredient, param);
  }

  @Override
  public List<StockIngredientInfo> findStockIngredientsByName(long userId, String name) {
    RowMapper<StockIngredientInfo> rowMapper = (rs, rownum) ->
        new StockIngredientInfo(rs.getLong("id"),
            rs.getString("ingredientsname"),
            rs.getString("type"),
            rs.getString("category"),
            rs.getBoolean("isactive"),
            rs.getLong("quantity"),
            rs.getString("image"));

    return jdbcTemplate.query(String.format(findStockIngredientsByName, userId, name + "%"),
        rowMapper);
  }

  @Override
  public List<StockIngredient> findExistingStockIngredientById(long userId, long ingredientId) {
    RowMapper<StockIngredient> rowMapper = (rs, rownum) ->
        new StockIngredient(rs.getLong("id"),
            rs.getLong("userid"),
            rs.getLong("ingredientid"),
            rs.getLong("quantity"));

    return jdbcTemplate.query(String.format(findExistingStockIngredientById, userId, ingredientId),
        rowMapper);
  }

  @Override
  public List<StockIngredientInfo> findStockIngredientsFiltered(long userId, String type,
                                                                String category) {
    RowMapper<StockIngredientInfo> rowMapper = (rs, rownum) ->
        new StockIngredientInfo(rs.getLong("id"),
            rs.getString("ingredientsname"),
            rs.getString("type"),
            rs.getString("category"),
            rs.getBoolean("isactive"),
            rs.getLong("quantity"),
            rs.getString("image"));

    return jdbcTemplate.query(String.format(findStockIngredientsFiltered, userId, type, category),
        rowMapper);
  }


}
