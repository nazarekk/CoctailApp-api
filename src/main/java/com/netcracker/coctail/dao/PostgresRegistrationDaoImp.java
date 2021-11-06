package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.Users;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class PostgresRegistrationDaoImp implements PostgresRegistrationDao {


    private JdbcTemplate jdbcTemplate;

    public PostgresRegistrationDaoImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create(Users user) {
        final String sql = "INSERT INTO users (email, password) VALUES (:email,:password)";
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword());
        jdbcTemplate.update(sql,param, holder);
    }

    @Override
    public Collection<Users> getAll() {
        RowMapper<Users> rowMapper = (rs, rownum) ->
                new Users(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("password"));
        Collection<Users> users = jdbcTemplate.query("SELECT id FROM users",rowMapper);
        return users;
    }

    public Collection<Integer> getAllId() {
        return jdbcTemplate.queryForList("SELECT id FROM users", Integer.class);
    }
}