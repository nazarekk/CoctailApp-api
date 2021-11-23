package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.ActivateModerator;
import com.netcracker.coctail.model.Moderator;
import com.netcracker.coctail.model.ModeratorInformation;
import com.netcracker.coctail.services.MailSender;
import java.util.Collection;
import java.util.UUID;
import lombok.Data;
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

@Data
@Component
@PropertySource("classpath:SQLscripts.properties")
public class ModeratorDaoImp implements ModeratorDao {

  private final NamedParameterJdbcTemplate jdbcTemplate;
  private final BCryptPasswordEncoder passwordEncoder;
  @Value("${moderatorCreation}")
  private String moderatorCreation;
  @Value("${ActivateModerator}")
  private String moderatorActivation;
  @Value("${getModerators}")
  private String getModerators;
  @Value("${EditModerator}")
  private String EditModerator;
  @Value("${RemoveModerator}")
  private String RemoveModerator;
  @Value("${SearchModerator}")
  private String SearchModerator;
  @Value("${FilterModerator}")
  private String FilterModerator;

  @Autowired
  private MailSender mailSender;

  @Async
  public void send(String email, String code) {
    String message =
        "Hello! To finish registration visit http://localhost:8080/api/moderators/activation/'%s'";
    mailSender.send(email, "verification", String.format(message, code));
  }

  @Override
  public int create(Moderator moderator) {
    String activation = UUID.randomUUID().toString();
    KeyHolder holder = new GeneratedKeyHolder();
    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("email", moderator.getEmail())
        .addValue("isactive", moderator.getIsactive())
        .addValue("activation", activation);
    int res = jdbcTemplate.update(moderatorCreation, param, holder);
    if (res == 1)
      send(moderator.getEmail(), activation);
    return res;
  }

  @Override
  public void activateModerator(ActivateModerator moderator) {
    KeyHolder holder = new GeneratedKeyHolder();
    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("roleid", 4)
        .addValue("activation", moderator.getCode())
        .addValue("nickname", moderator.getNickname())
        .addValue("password", passwordEncoder.encode(moderator.getPassword()));
    jdbcTemplate.update(moderatorActivation, param, holder);
  }

  @Override
  public Collection<ModeratorInformation> moderatorList() {
    RowMapper<ModeratorInformation> rowMapper = (rs, rowNum) ->
        new ModeratorInformation(
            rs.getLong("userid"),
            rs.getString("email"),
            rs.getString("nickname"),
            rs.getBoolean("isactive"));
    return jdbcTemplate.query(getModerators, rowMapper);
  }

  @Override
  public void editModerator(ModeratorInformation moderator) {
    KeyHolder holder = new GeneratedKeyHolder();
    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("userid", moderator.getUserid())
        .addValue("email", moderator.getEmail())
        .addValue("nickname", moderator.getNickname())
        .addValue("isactive", moderator.getIsActive());
    jdbcTemplate.update(EditModerator, param, holder);
  }

  @Override
  public void removeModerator(ModeratorInformation moderator) {
    KeyHolder holder = new GeneratedKeyHolder();
    SqlParameterSource param = new MapSqlParameterSource()
        .addValue("userid", moderator.getUserid());
    jdbcTemplate.update(RemoveModerator, param, holder);
  }

  @Override
  public ModeratorInformation searchModerator(String value) {
    RowMapper<ModeratorInformation> rowMapper = (rs, rowNum) ->
        new ModeratorInformation(
            rs.getLong("userid"),
            rs.getString("email"),
            rs.getString("nickname"),
            rs.getBoolean("isactive"));
    return jdbcTemplate.query(String.format(SearchModerator, value), rowMapper).get(0);
  }

  @Override
  public ModeratorInformation filterModerator(Boolean isactive) {
    RowMapper<ModeratorInformation> rowMapper = (rs, rowNum) ->
        new ModeratorInformation(
            rs.getLong("userid"),
            rs.getString("email"),
            rs.getString("nickname"),
            rs.getBoolean("isactive"));
    return jdbcTemplate.query(String.format(FilterModerator, isactive), rowMapper).get(0);
  }

}