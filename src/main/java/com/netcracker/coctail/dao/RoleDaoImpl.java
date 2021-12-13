package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@PropertySource("classpath:SQLscripts.properties")
public class RoleDaoImpl implements RoleDao {

    @Value("${findRoleByEmail}")
    private String findRoleByEmail;

    private final JdbcTemplate jdbcTemplate;

    public RoleDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    RowMapper<Role> rowMapper = (rs, rownum) ->
            new Role(rs.getLong("id"),
                    rs.getString("rolename"));

    @Override
    public List<Role> findRoleNameByEmail(String email) {
        return jdbcTemplate.query(String.format(findRoleByEmail, email), rowMapper);

    }

}
