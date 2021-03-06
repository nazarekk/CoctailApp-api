package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.UserInfo;
import com.netcracker.coctail.model.UserPersonalInfo;
import com.netcracker.coctail.model.UserPhoto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.RowMapper;
import com.netcracker.coctail.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@PropertySource("classpath:SQLscripts.properties")
public class UserDaoImpl implements UserDao {

  @Value("${findUserByEmail}")
  private String findUserByEmail;
  @Value("${findUserById}")
  private String findUserById;
  @Value("${userInfo}")
  private String userInfo;
  @Value("${EditUser}")
  private String EditUser;
  @Value("${personalInfo}")
  private String personalInfo;
  @Value("${FindInfoByNickname}")
  private String findInfoByNickname;
  @Value("${editUserPhoto}")
  private String editUserPhoto;
  @Value("${getUserPhoto}")
  private String getUserPhoto;


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
          rs.getBoolean("isactive"),
          rs.getString("image"));

  @Override
  public List<User> findUserByEmail(String email) {
    return jdbcTemplate.query(String.format(findUserByEmail, email), rowMapper);
  }

  @Override
  public List<User> findUserById(Long id) {
    return jdbcTemplate.query(String.format(findUserById, id), rowMapper);
  }

  @Override
  public UserInfo myInfo(String email) {
    RowMapper<UserInfo> rowMapper = (rs, rownum) ->
        new UserInfo(rs.getString("nickname"),
            rs.getString("email"));
    return jdbcTemplate.query(String.format(userInfo, email), rowMapper).get(0);
  }

  @Override
  public UserPersonalInfo getInfo(String email) {
    RowMapper<UserPersonalInfo> rowMapper = (rs, rownum) ->
            new UserPersonalInfo(rs.getString("nickname"),
                    rs.getString("information"),
                    rs.getString("image"));
    return jdbcTemplate.query(String.format(personalInfo, email), rowMapper).get(0);
  }

  @Override
  public int editInfo(String email, UserPersonalInfo user) {
    return jdbcTemplate.update(EditUser, user.getNickname(), user.getInformation(), email);
  }
  @Override
  public List<UserPersonalInfo> findUsersByNickname(String email, UserPersonalInfo user) {
    RowMapper<UserPersonalInfo> rowMapper = (rs, rownum) ->
            new UserPersonalInfo(rs.getString("nickname"),
                    rs.getString("information"),
                    rs.getString("image"));
    return jdbcTemplate.query(String.format(findInfoByNickname, user.getNickname(), email), rowMapper);
  }

  @Override
  public int editPhoto(String email, UserPhoto user) {
    return jdbcTemplate.update(editUserPhoto, user.getPhoto(), email);
  }

  @Override
  public String getUserPhoto(String email) {
    RowMapper<UserPhoto> rowMapper = (rs, rownum) ->
            new UserPhoto(rs.getString("image"));
    return jdbcTemplate.query(String.format(getUserPhoto, email), rowMapper).get(0).getPhoto();
  }

}
