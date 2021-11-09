package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.ReadUser;
import com.netcracker.coctail.model.CreateUser;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.regex.Pattern;

@Repository
public class PostgresRegistrationDaoImp implements PostgresRegistrationDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PostgresRegistrationDaoImp(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static boolean emailCheck(String email) {
        Pattern checkE = Pattern.compile(
                "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z][a-z](?:[a-z]*[a-z])?$"
        );
        return (checkE.matcher(email).matches());
    }

    public static boolean passwordCheck(String password) {
        Pattern checkP = Pattern.compile(
                "^[a-zA-Z0-9_]{5,}$"
        );
        if (password.matches(".*\\d+.*") & (password.matches(".*[a-z]+.*")) & (password.matches(".*[A-Z]+.*"))) {
            return (checkP.matcher(password).matches() & password.length() > 5);
        }
        return false;
    }


    @Override
    public String create(CreateUser user) {
        final String sql = "INSERT INTO users (email, password) VALUES (:email, :password)";
        KeyHolder holder = new GeneratedKeyHolder();
        if (emailCheck(user.getEmail())) {
            if (passwordCheck(user.getPassword())) {
                if(user.getVerification().equals(user.getPassword())){
                    SqlParameterSource param = new MapSqlParameterSource()
                            .addValue("email", user.getEmail())
                            .addValue("password", user.getPassword());
                    jdbcTemplate.update(sql, param, holder);
                    return "user created successfully!";
                }
                else{
                    return "passwords do not match";
                }
            }
            else {
                return "invalid password";
            }
        }
        else {
            return "invalid email";
        }
    }

    @Override
    public Collection<ReadUser> getAll() {
        RowMapper<ReadUser> rowMapper = (rs, rownum) ->
                new ReadUser(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("password"));
        return jdbcTemplate.query("SELECT id, email, password FROM users", rowMapper);
    }
}
