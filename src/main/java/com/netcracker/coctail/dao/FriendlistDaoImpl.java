package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.Friendlist;
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
    public int createFriendlist(long ownerid, long friendid, long statusid) {
        KeyHolder holder = new GeneratedKeyHolder();
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("ownerid", ownerid)
                .addValue("friendid", friendid)
                .addValue("statusid", statusid);
        return jdbcTemplate.update(friendlistCreation, param, holder);
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
}
