package com.netcracker.coctail.dao;

import java.util.List;

import com.netcracker.coctail.model.CreateKitchenware;
import com.netcracker.coctail.model.Kitchenware;
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
public class KitchenwareDaoImp implements KitchenwareDao {

  @Value("${findAllKitchenwareByName}")
  private String findAllKitchenwareByName;
  @Value("${findAllKitchenwareFiltered}")
  private String findAllKitchenwareFiltered;
  @Value("${findAllKitchenwareFilteredWithoutActive}")
  private String findAllKitchenwareFilteredWithoutActive;
  @Value("${findKitchenwareByName}")
  private String findKitchenwareByName;
  @Value("${findKitchenwareById}")
  private String findKitchenwareById;
  @Value("${createKitchenware}")
  private String createKitchenware;
  @Value("${editKitchenware}")
  private String editKitchenware;
  @Value("${removeKitchenware}")
  private String removeKitchenware;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    RowMapper<Kitchenware> rowMapper = (rs, rownum) ->
            new Kitchenware(rs.getLong("id"),
                    rs.getString("kitchenwarename"),
                    rs.getString("type"),
                    rs.getString("category"),
                    rs.getBoolean("isActive"),
                    rs.getString("image"));

    @Override
    public void create(CreateKitchenware kitchenware) {
      String link;
      if (kitchenware.getImage() == null) {
        link =
            "https://static.thenounproject.com/png/1738131-200.png";
      } else {
        link = kitchenware.getImage();
      }
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("kitchenwarename", kitchenware.getName())
                .addValue("type", kitchenware.getType())
                .addValue("category", kitchenware.getCategory())
                .addValue("isActive", kitchenware.isActive())
                .addValue("image", link);
        jdbcTemplate.update(createKitchenware, param);
    }

  @Override
  public List<Kitchenware> findAllKitchenwareByName(String name) {
    return jdbcTemplate.query(String.format(findAllKitchenwareByName, name + "%"), rowMapper);
  }

  @Override
  public List<Kitchenware> findAllKitchenwareFiltered(String type, String category, String active) {
      if (active == "true" || active == "false") {
          return jdbcTemplate.query(String.format(findAllKitchenwareFiltered,
              "%" + type + "%", "%" + category + "%", Boolean.valueOf(active)), rowMapper);
      }
    return jdbcTemplate.query(String.format(findAllKitchenwareFilteredWithoutActive,
        "%" + type + "%", "%" + category + "%"), rowMapper);
  }

  @Override
  public List<Kitchenware> findKitchenwareByName(String name) {
    return jdbcTemplate.query(String.format(findKitchenwareByName, name), rowMapper);
  }

  @Override
  public List<Kitchenware> findKitchenwareById(Long id) {
    return jdbcTemplate.query(String.format(findKitchenwareById, id), rowMapper);
  }

    @Override
    public void editKitchenware(Kitchenware kitchenware) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", kitchenware.getId())
                .addValue("kitchenwarename", kitchenware.getName())
                .addValue("type", kitchenware.getType())
                .addValue("category", kitchenware.getCategory())
                .addValue("isActive", kitchenware.isActive())
                .addValue("image", kitchenware.getImage());
        jdbcTemplate.update(editKitchenware, param);
    }

  @Override
  public void removeKitchenware(Kitchenware kitchenware) {
    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("id", kitchenware.getId());
    jdbcTemplate.update(removeKitchenware, param);
  }

}
