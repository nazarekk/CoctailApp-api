package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.Friendlist;
import com.netcracker.coctail.model.FriendsStatus;
import com.netcracker.coctail.model.ReadUser;
import com.netcracker.coctail.model.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@PropertySource("classpath:SQLscripts.properties")
public class FriendlistDaoImpl implements FriendlistDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    @Value("${friendlistCreation}")
    private String friendlistCreation;
    @Value("${EditFriendlist}")
    private String EditFriendlist;
    @Value("${RemoveFriendlist}")
    private String RemoveFriendlist;
    @Value("${FindFriendlist}")
    private String FindFriendlist;
    @Value("${FindStatusId}")
    private String FindStatusId;
    @Value("${FindIdByEmail}")
    private String FindIdByEmail;
    @Value("${FindIdsByNickname}")
    private String FindIdsByNickname;

    @Override
    public List<Friendlist> findFriendlist(long ownerid, long friendid) {
        RowMapper<Friendlist> rowMapper = (rs, rownum) ->
                new Friendlist(rs.getLong("id"),
                        rs.getLong("ownerid"),
                        rs.getLong("friendid"),
                        rs.getLong("statusid"));
        return jdbcTemplate.query(String.format(FindFriendlist, ownerid, friendid), rowMapper);
    }

    @Override
    public void createFriendlist(long ownerid, long friendid, long statusid) {
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("ownerid", ownerid)
                .addValue("friendid", friendid)
                .addValue("statusid", statusid);
        jdbcTemplate.update(friendlistCreation, param, holder);
    }

    @Override
    public void editFriendlist(long ownerid, long friendid, long statusid) {
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("ownerid", ownerid)
                .addValue("friendid", friendid)
                .addValue("statusid", statusid);
        jdbcTemplate.update(EditFriendlist, param, holder);
    }

    @Override
    public void removeFriendlist(long ownerid, long friendid) {
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("ownerid", ownerid)
                .addValue("friendid", friendid);
        jdbcTemplate.update(RemoveFriendlist, param, holder);
    }

    @Override
    public long getStatusId(String status) {
        RowMapper<FriendsStatus> rowMapper = (rs, rownum) ->
                new FriendsStatus(
                        rs.getLong("id"),
                        rs.getString("statusname"));
        return jdbcTemplate.query(String.format(FindStatusId, status), rowMapper).get(0).getId();
    }

    @Override
    public long getOwnerId(String email) {
        RowMapper<ReadUser> rowMapper = (rs, rownum) ->
                new ReadUser(
                        rs.getLong("userid"),
                        rs.getString("email"),
                        rs.getLong("roleid"));
        return jdbcTemplate.query(String.format(FindIdByEmail, email), rowMapper).get(0).getUserid();
    }

    @Override
    public List<User> getOwnerByNickname(String nickname) {
        RowMapper<User> rowMapper = (rs, rownum) ->
                new User(rs.getLong("userid"),
                        rs.getString("nickname"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getLong("roleid"),
                        rs.getBoolean("isactive"));
        List<User> list = jdbcTemplate.query(String.format(FindIdsByNickname, nickname + "%"), rowMapper);
        System.out.println(list.get(0).getNickname());
        /*int count = list.size();
        long[] ids = new long[count];
        for (int i = 0; i < count; i++) {
            ids[i] = list.get(i).getId();
        }*/
        return list;
    }
}
