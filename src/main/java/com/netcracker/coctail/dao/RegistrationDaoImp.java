package com.netcracker.coctail.dao;


import com.netcracker.coctail.model.ReadUser;
import com.netcracker.coctail.model.CreateUser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import com.netcracker.coctail.services.MailSender;

import java.util.UUID;

@Repository
@Data
@Component
@Slf4j
public class RegistrationDaoImp implements RegistrationDao {

    private final BCryptPasswordEncoder passwordEncoder;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private MailSender mailSender;

    @Value("${regUser}")
    private String userCreation;
    @Value("${activateUser}")
    private String userActivation;

    @Async
    public void send(String email, String code) {
        String message = "Hello! To finish registration visit http://localhost:8080/api/users/activation/'%s'";
        mailSender.send(email, "verification", String.format(message, code));

    }

    @Override
    public String create(CreateUser user) {
        String activation = UUID.randomUUID().toString();
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("password", passwordEncoder.encode(user.getPassword()))
                .addValue("activation", activation);
        jdbcTemplate.update(userCreation, param, holder);
        send(user.getEmail(), activation);
        return "user created successfully!";
    }

    @Override
    public void activateUser(String code) {
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
            .addValue("roleid", 2)
            .addValue("activation", code);
        jdbcTemplate.update(userActivation, param);
    }
}
