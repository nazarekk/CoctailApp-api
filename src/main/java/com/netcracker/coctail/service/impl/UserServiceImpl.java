package com.netcracker.coctail.service.impl;

import com.netcracker.coctail.dao.ForgotPasswordDao;
import com.netcracker.coctail.exceptions.InvalidEmailOrPasswordException;
import com.netcracker.coctail.exceptions.InvalidNicknameException;
import com.netcracker.coctail.exceptions.InvalidPasswordException;
import com.netcracker.coctail.model.*;
import com.netcracker.coctail.dao.RoleDao;
import com.netcracker.coctail.dao.UserDao;
import com.netcracker.coctail.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {UserService} interface.
 */

@Service
@Slf4j
@Data
public class UserServiceImpl implements UserService {

    private UserDao userDao;
    private RoleDao roleDao;
    private JdbcTemplate jdbcTemplate;
    private ForgotPasswordDao forgotPasswordDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Lazy
    public UserServiceImpl(UserDao userDao, RoleDao roleDao,
                           JdbcTemplate jdbcTemplate,
                           ForgotPasswordDao forgotPasswordDao,
                           PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.jdbcTemplate = jdbcTemplate;
        this.forgotPasswordDao = forgotPasswordDao;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserByEmail(String email) {
        List<User> result = userDao.findUserByEmail(email);
        if (result.isEmpty()) {
            throw new InvalidEmailOrPasswordException();
        }
        log.info("IN getUserByEmail - user: {} found by email: {}", result.get(0).getNickname(), result.get(0).getEmail());
        return result.get(0);
    }

    @Override
    public String getRolenameByEmail(String email) {
        Role result = roleDao.findRoleNameByEmail(email).get(0);
        log.info("IN getRolenameByEmail - role: {} found by email: {}", result.getRolename(), email);
        return result.getRolename();
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

    public String changeUserPassword(User user, String password) {
        if (forgotPasswordDao.changePassword(user, password) != 1) {
            log.warn("Change user password by email: {}, not succsessful", user.getEmail());
            return null;
        } else {
            log.warn("Change user password by email: {}, successfuly", user.getEmail());
            return "Password changed successfuly";
        }
    }

    public void checkPassword(User user, UserPasswords userPasswords) {
        if (!passwordEncoder.matches(userPasswords.getOldPassword(), user.getPassword())) {
            log.warn("CheckPassword: user by email: {}, not succsessful", user.getEmail());
            throw new InvalidPasswordException();
        } else {
            changeUserPassword(user, userPasswords.getNewPassword());
        }
    }

    public void changeInfo(String email, UserPersonalInfo user) {
        if (!userDao.findUsersByNickname(email, user).isEmpty()) {
            throw new InvalidNicknameException();
        } else {
            userDao.editInfo(email, user);
            log.warn("EditInfo: user by email: {}, change personal info succsessful", email);
        }
    }

    public boolean changeUserPhoto(String email, UserPhoto photo) {
        int check = userDao.editPhoto(email, photo);
        if (check == 1) {
            log.info("EditPhoto: user by email: {}, change photo successful", email);
            return true;
        }
        else {
            log.warn("EditPhoto: user by email: {}, change photo NOT successful", email);
            return false;
        }
    }
}
