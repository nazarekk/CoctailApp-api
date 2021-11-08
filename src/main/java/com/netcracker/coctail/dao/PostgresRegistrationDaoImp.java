package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.Users;
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

    private NamedParameterJdbcTemplate jdbcTemplate;

    public PostgresRegistrationDaoImp(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public static boolean emailCheck(String email){
        return (checkE.matcher(email).matches())	;
    }

    public static boolean passwordCheck(String password){
        boolean ok = false;
        if(password.matches(".*\\d+.*")){
            if(password.matches(".*[a-z]+.*")){
                if(password.matches(".*[A-Z]+.*")){
                    ok = true;
                }
            }
        };
        return (checkP.matcher(password).matches()&&ok&&password.length()>5);
    }

    private static final Pattern checkE = Pattern.compile(
            "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z][a-z](?:[a-z]*[a-z])?$"
    );

    private static final Pattern checkP = Pattern.compile(
            "^[a-zA-Z0-9_]{5,}$"
    );

    @Override
    public void create(Users user) {
        final String sql = "INSERT INTO users (id, email, password) VALUES (:id, :email, :password)";
        KeyHolder holder = new GeneratedKeyHolder();
        if(emailCheck(user.getEmail())) {
            if (passwordCheck(user.getPassword())) {
                    SqlParameterSource param = new MapSqlParameterSource()
                            .addValue("id", user.getId())
                            .addValue("email", user.getEmail())
                            .addValue("password", user.getPassword());
                    jdbcTemplate.update(sql, param, holder);
            }
        }
    }

    @Override
    public Collection<Users> getAll() {
        RowMapper<Users> rowMapper = (rs, rownum) ->
                new Users(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("password"));
        Collection<Users> users = jdbcTemplate.query("SELECT id, email, password FROM users",rowMapper);
        return users;
    }

   /* public Collection<Integer> getAllId() {
        return jdbcTemplate.queryForList("SELECT id FROM users", Integer.class);
    }*/
}
