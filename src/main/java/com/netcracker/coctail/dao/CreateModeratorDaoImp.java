package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.ActivateModerator;
import com.netcracker.coctail.model.Moderator;
import com.netcracker.coctail.services.MailSender;
import java.util.UUID;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Data
@Component
public class CreateModeratorDaoImp implements CreateModeratorDao {

  private final NamedParameterJdbcTemplate jdbcTemplate;
  private final BCryptPasswordEncoder passwordEncoder;
  @Value("${moderatorCreation}")
  private String moderatorCreation;
  @Value("${activateModerator}")
  private String moderatorActivation;

  @Autowired
  private MailSender mailSender;

  @Async
  public void send(String email, String code) {
    String message = "Hello! To finish registration visit http://localhost:8080/api/moderators/activation/'%s'";
    mailSender.send(email, "verification", String.format(message, code));

  }

  @Override
  public String create (Moderator moderator) {
    String activation = UUID.randomUUID().toString();
    KeyHolder holder = new GeneratedKeyHolder();
    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("email", moderator.getEmail())
        .addValue("active",moderator.getIsActive())
        .addValue("activation", activation);
    jdbcTemplate.update(moderatorCreation, param, holder);
    send(moderator.getEmail(), activation);
    return "User created";
  }

  @Override
  public void activateModerator(ActivateModerator moderator) {
    KeyHolder holder = new GeneratedKeyHolder();
    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("roleid", 4)
        .addValue("activation", moderator.getCode())
        .addValue("password", passwordEncoder.encode(moderator.getPassword()));
    jdbcTemplate.update(moderatorActivation, param, holder);
  }

}
