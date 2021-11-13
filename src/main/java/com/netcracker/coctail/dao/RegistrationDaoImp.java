package com.netcracker.coctail.dao;


import com.netcracker.coctail.model.ReadUser;
import com.netcracker.coctail.model.CreateUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import com.netcracker.coctail.services.MailSender;

import java.util.List;
import java.util.UUID;

@Repository
public class RegistrationDaoImp implements RegistrationDao {

    @Autowired
    private MailSender mailSender;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public RegistrationDaoImp(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Async
    public void send(String email, String code) {
        String message = "Hello! To finish registration visit http://localhost:8080/api/users/activation/'%s'";
        mailSender.send(email, "verification", String.format(message, code));

    }

    @Override
    public String create(CreateUser user) {
        final String sql = "INSERT INTO users (email, password, activation) VALUES (:email, :password, :activation)";
        String activation = UUID.randomUUID().toString();
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("password", user.getPassword())
                .addValue("activation", activation);
        jdbcTemplate.update(sql, param, holder);
        send(user.getEmail(), activation);
        return "user created successfully!";
    }

    @Override
    public List<ReadUser> getByCode(String code) {
        String sql = "SELECT userid, email FROM users WHERE activation = '%s'";
        RowMapper<ReadUser> rowMapper = (rs, rownum) ->
                new ReadUser(
                        rs.getInt("userid"),
                        rs.getString("email"));
        return jdbcTemplate.query(String.format(sql, code), rowMapper);
    }

    public void activateUser(String code) {
        List<ReadUser> users = getByCode(code);
        users.get(0).setRoleId(2);
    }
}
