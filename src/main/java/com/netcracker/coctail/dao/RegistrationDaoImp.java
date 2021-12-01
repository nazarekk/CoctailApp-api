package com.netcracker.coctail.dao;


import com.netcracker.coctail.model.ActivateUser;
import com.netcracker.coctail.model.CreateUser;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import com.netcracker.coctail.services.MailSender;

import java.util.UUID;

@Repository
@Data
@Slf4j
@Component
@PropertySource("classpath:SQLscripts.properties")
public class RegistrationDaoImp implements RegistrationDao {

    private PasswordEncoder passwordEncoder;
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    RegistrationDaoImp(PasswordEncoder passwordEncoder, NamedParameterJdbcTemplate jdbcTemplate) {
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    private MailSender mailSender;

    @Value("${regUser}")
    private String userCreation;
    @Value("${ActivateUser}")
    private String userActivation;
    @Value("${front_link}")
    private String front_link;

    @Async
    public void send(String email, String code) {
        String message = "Hello! To finish registration visit "
            + front_link + "/registration/verification?code=" + code;
        mailSender.send(email, "verification", message);
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
    public int activateUser(ActivateUser activateUser) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("roleid", 2)
                .addValue("activation", activateUser.getVerificationCode())
                .addValue("nickname", activateUser.getNickname());
        return jdbcTemplate.update(userActivation, param);
    }
}
