package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.*;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.Collection;
import java.util.List;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;



@Data
@Component
@PropertySource("classpath:SQLscripts.properties")
public class KitchenwareDaoImp implements KitchenwareDao {


    @Value("SELECT Kitchenwareid, name, type, category, isactive FROM kitchenware WHERE name = '%s';")
    private String findKitchenwareByName;
    @Value("SELECT Kitchenwareid, name, type, category, isactive FROM kitchenware WHERE kitchenwareid = '%s';")
    private String findKitchenwareById;
    @Value  ("SELECT Kitchenwareid, name, type, category, isactive FROM kitchenware WHERE isactive = '%s';")
    private String filterKitchenware;
    @Value  ("INSERT INTO Kitchenware (id, name,type,category, isactive) values (:id, :name,:type,:category,:isactive);")
    private String createKitchenware;
    @Value  ("UPDATE kitchenware set name = :name, type = :type, category = :category, isactive = :isactive WHERE kitchenwareid = :kitchenwareid;")
    private String editKitchenware;
    @Value  ("SELECT Kitchenwareid, name, type, category, isactive FROM kitchenware")
    private String getKitchenware;
    @Value  ("DELETE FROM Kitchenware WHERE kitchenwareid = :kitchenwareid;")
    private String removeKitchenware;

    private final JdbcTemplate jdbcTemplate;



    RowMapper<Kitchenware> rowMapper = (rs, rownum) ->
            new Kitchenware(rs.getLong("kitchenwareid"),
                    rs.getString("name"),
                    rs.getString("type"),
                    rs.getString("category"),
                    rs.getBoolean("isactive"));

    @Override
    public int create(Kitchenware kitchenware) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("kitchenwareid", kitchenware.getId())
                .addValue("name", kitchenware.getName())
                .addValue("type", kitchenware.getType())
                .addValue("category", kitchenware.getCategory())
                .addValue("isactive", kitchenware.isActive());

        return jdbcTemplate.update(createKitchenware, param);
    }

    public Collection<Kitchenware> KitchenwareList() {
        RowMapper<Kitchenware> rowMapper = (rs, rowNum) ->
                new Kitchenware(
                        rs.getLong("kitchenwareid"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("category"),
                        rs.getBoolean("isactive"));
        return jdbcTemplate.query(getKitchenware, rowMapper);
    }

    @Override
    public Collection<Kitchenware> getKitchenware(){
        RowMapper<Kitchenware> rowMapper = (rs, rowNum) ->
                new Kitchenware(
                        rs.getLong("kitchenwareid"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("category"),
                        rs.getBoolean("isactive"));
        return jdbcTemplate.query(getKitchenware, rowMapper);
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
                .addValue("ingredientid", kitchenware.getId())
                .addValue("name", kitchenware.getName())
                .addValue("type", kitchenware.getType())
                .addValue("category", kitchenware.getCategory())
                .addValue("isactive", kitchenware.isActive());
        jdbcTemplate.update(editKitchenware, param);
    }

    @Override
    public void removeKitchenware(Kitchenware kitchenware) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("kitchenwareid", kitchenware.getId());
        jdbcTemplate.update(removeKitchenware, param);
    }


    public Kitchenware filterKitchenware(Boolean IsActive) {
        RowMapper<Kitchenware> rowMapper = (rs, rowNum) ->
                new Kitchenware(
                        rs.getLong("kitchenwareid"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("category"),
                        rs.getBoolean("isactive"));
        return jdbcTemplate.query(String.format(filterKitchenware, IsActive), rowMapper).get(0);
    }


}
