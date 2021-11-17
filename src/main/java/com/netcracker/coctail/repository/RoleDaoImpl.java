package com.netcracker.coctail.repository;

import com.netcracker.coctail.model.Role;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao {

    private static final String FIND_ROLE_BY_EMAIL = "SELECT id, rolename FROM role INNER JOIN users ON role.id = users.roleid WHERE email = '%s'";

    private final JdbcTemplate jdbcTemplate;

    public RoleDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    RowMapper<Role> rowMapper = (rs, rownum) ->
            new Role(rs.getLong("id"),
                    rs.getString("rolename"));

    @Override
    public List<Role> findRoleNameByEmail(String email) {
        return jdbcTemplate.query(String.format(FIND_ROLE_BY_EMAIL, email), rowMapper);

    }

}
