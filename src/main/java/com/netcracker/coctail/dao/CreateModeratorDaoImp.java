package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.Moderator;
import com.netcracker.coctail.model.ModeratorConfirmation;
import com.netcracker.coctail.model.ReadUser;
import com.netcracker.coctail.services.MailSender;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Data
@Component
public class CreateModeratorDaoImp implements CreateModeratorDao {

  private final NamedParameterJdbcTemplate jdbcTemplate;
  @Value("${moderatorCreation}")
  private String moderatorCreation;
  @Value("${activateUser}")
  private String userActivation;

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
        .addValue("nickname",moderator.getNickname())
        .addValue("isActive",moderator.getIsActive())
        .addValue("activation", activation);
    jdbcTemplate.update(moderatorCreation, param, holder);
    send(moderator.getEmail(), activation);
    return "User created";
  }

  @Override
  public void activateModerator(String code, ModeratorConfirmation user) {
    List<ReadUser> users = getByCode(code);
    users.get(0).setRoleId(4);
  }

  @Override
  public List<ReadUser> getByCode(String code) {
    RowMapper<ReadUser> rowMapper = (rs, rownum) ->
        new ReadUser(
            rs.getInt("userid"),
            rs.getString("email"));
    return jdbcTemplate.query(String.format(userActivation, code), rowMapper);
  }

}
