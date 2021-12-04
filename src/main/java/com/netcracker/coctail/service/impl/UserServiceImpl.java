package com.netcracker.coctail.service.impl;

import com.netcracker.coctail.dao.ForgotPasswordDao;
import com.netcracker.coctail.exceptions.InvalidEmailOrPasswordException;
import com.netcracker.coctail.exceptions.InvalidNicknameException;
import com.netcracker.coctail.exceptions.InvalidPasswordException;
import com.netcracker.coctail.model.Role;
import com.netcracker.coctail.model.User;
import com.netcracker.coctail.dao.RoleDao;
import com.netcracker.coctail.dao.UserDao;
import com.netcracker.coctail.model.UserPasswords;
import com.netcracker.coctail.model.UserPersonalInfo;
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
    private final JdbcTemplate jdbcTemplate;
    private final ForgotPasswordDao forgotPasswordDao;
    private final BCryptPasswordEncoder passwordEncoder;

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
            log.warn("Change user password by email: {}, succsessfuly", user.getEmail());
            return "Password changed succsessfuly";
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
}
