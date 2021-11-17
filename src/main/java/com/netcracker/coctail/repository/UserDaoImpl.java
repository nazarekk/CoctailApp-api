package com.netcracker.coctail.repository;

import org.springframework.jdbc.core.RowMapper;
import com.netcracker.coctail.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    private static final String FIND_BY_EMAIL = "SELECT userid, nickname, email, password, roleid, isactive FROM users WHERE email = '%s'";
    private static final String FIND_BY_ID = "SELECT userid, nickname, email, password, roleid, isactive FROM users WHERE userid = '%s'";

    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    RowMapper<User> rowMapper = (rs, rownum) ->
            new User(rs.getLong("userid"),
                    rs.getString("nickname"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getLong("roleid"),
                    rs.getBoolean("isactive"));

    @Override
    public List<User> findUserByEmail(String email) {
        return jdbcTemplate.query(String.format(FIND_BY_EMAIL, email), rowMapper);
    }

    @Override
    public List<User> findUserById(Long id) {
        return jdbcTemplate.query(String.format(FIND_BY_ID, id), rowMapper);
    }
}
