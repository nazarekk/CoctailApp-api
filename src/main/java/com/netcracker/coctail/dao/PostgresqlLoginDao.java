package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.Login;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

@Repository
public class PostgresqlLoginDao implements LoginDao {
    private static final String SELECT_GET_ALL = "SELECT id, email, password FROM users";
    private static final String SELECT_USER = "SELECT id, email, password FROM users WHERE email = '%s' AND password = '%s'";

    private final JdbcTemplate jdbcTemplate;

    public PostgresqlLoginDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Map<String, Login> logins;

    @Override
    public Login read(String id) {
        return logins.get(id);
    }

    /*
        @Override
        public void update(Login login) {
            logins.put(login.getId(), login);
        }
    */
    @Override
    public boolean checkByEmailAndPassword(String email, String password) {
        Collection<Login> query = jdbcTemplate.query(String.format(SELECT_USER, email, password), PostgresqlLoginDao::mapUserRow);
        return !query.isEmpty();
    }

    public Collection<Login> getAll() {
        return jdbcTemplate.query(SELECT_GET_ALL, PostgresqlLoginDao::mapUserRow);
    }

    private static Login mapUserRow(ResultSet rs, int rowNum) throws SQLException {
        return new Login(rs.getInt("id"),
                rs.getString("email"),
                rs.getString("password"));
    }
}

