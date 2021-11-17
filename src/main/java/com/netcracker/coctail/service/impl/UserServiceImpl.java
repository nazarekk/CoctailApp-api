package com.netcracker.coctail.service.impl;

import com.netcracker.coctail.exception.InvalidEmailOrPasswordException;
import com.netcracker.coctail.model.Role;
import com.netcracker.coctail.model.User;
import com.netcracker.coctail.repository.RoleDao;
import com.netcracker.coctail.repository.UserDao;
import com.netcracker.coctail.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {UserService} interface.
 */

@Service
@Slf4j
@Data
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;


    public User getUserByEmail(String email) {
        List<User> result = userDao.findUserByEmail(email);
        if (result.isEmpty()) {
            throw new InvalidEmailOrPasswordException();
        }
        log.info("IN getUserByEmail - user: {} found by email: {}", result.get(0).getNickname(), result.get(0).getEmail());
        return result.get(0);
    }

    @Override
    public List<Role> getRolesByEmail(String email) {
        List<Role> name = roleDao.findRoleNameByEmail(email);
        return name;
    }

    @Override
    public User getUserById(Long id) {
        User result = userDao.findUserById(id).get(0);

        if (result == null) {
            log.warn("IN getUserById - no user found by id: {}", id);
            return null;
        }

        log.info("IN getUserById - user: {} found by id: {}", result.getNickname(), result.getId());
        return result;
    }
}
