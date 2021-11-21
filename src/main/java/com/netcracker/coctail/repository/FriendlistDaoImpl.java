package com.netcracker.coctail.repository;

import com.netcracker.coctail.model.Friendlist;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FriendlistDaoImpl implements FriendlistDao{

    private static final String FIND_FRIENDIST = "SELECT id, ownerid, friendid, statusid FROM friendlist WHERE ownerid = '%s' AND friendid = '%s'";

    private final JdbcTemplate jdbcTemplate;

    public FriendlistDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    RowMapper<Friendlist> rowMapper = (rs, rownum) ->
            new Friendlist(rs.getLong("id"),
                    rs.getLong("ownerid"),
                    rs.getLong("friendid"),
                    rs.getLong("statusid"));

    @Override
    public List<Friendlist> findFriendlist(long ownerid, long friendid) {
        return jdbcTemplate.query(String.format(FIND_FRIENDIST, ownerid, friendid), rowMapper);
    }
}
