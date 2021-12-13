package com.netcracker.coctail.dao;


import com.netcracker.coctail.model.FriendUser;
import com.netcracker.coctail.model.Friendlist;
import com.netcracker.coctail.model.FriendsStatus;
import com.netcracker.coctail.model.ReadUser;
import com.netcracker.coctail.model.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Data
@Component
@PropertySource("classpath:SQLscripts.properties")
public class FriendlistDaoImpl implements FriendlistDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final JdbcTemplate batchJdbcTemplate;

    @Value("${friendlistCreation}")
    private String friendlistCreation;
    @Value("${friendlist}")
    private String friendlist;
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
    public List<Friendlist> findFriendlist(long ownerId, long friendId) {
        RowMapper<Friendlist> rowMapper = (rs, rownum) ->
                new Friendlist(rs.getLong("id"),
                        rs.getLong("ownerid"),
                        rs.getLong("friendid"),
                        rs.getLong("statusid"));
        return jdbcTemplate.query(String.format(FindFriendlist, ownerId, friendId), rowMapper);
    }

    @Override
    public List<FriendUser> friendList(long ownerId) {
        RowMapper<FriendUser> rowMapper = (rs, rownum) ->
                new FriendUser(rs.getLong("userid"),
                        rs.getString("nickname"),
                        rs.getString("email"),
                        rs.getLong("statusid"));
        return jdbcTemplate.query(String.format(friendlist, ownerId), rowMapper);
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
    public void batchFriendlist(long ownerid, List<User> noFriendlist) {
         batchJdbcTemplate.batchUpdate(
                "INSERT INTO friendlist (ownerid, friendid, statusid) VALUES (?,?,?)",
                new BatchPreparedStatementSetter() {

                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, ownerid);
                        ps.setLong(2, noFriendlist.get(i).getId());
                        ps.setInt(3, 1);
                    }

                    public int getBatchSize() {
                        return noFriendlist.size();
                    }

                });
        batchJdbcTemplate.batchUpdate(
                "INSERT INTO friendlist (friendid, ownerid, statusid) VALUES (?,?,?)",
                new BatchPreparedStatementSetter() {

                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, ownerid);
                        ps.setLong(2, noFriendlist.get(i).getId());
                        ps.setInt(3, 1);
                    }

                    public int getBatchSize() {
                        return noFriendlist.size();
                    }

                });
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
        return list;
    }
}
