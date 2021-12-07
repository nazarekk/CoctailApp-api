package com.netcracker.coctail.service;

import com.netcracker.coctail.model.User;
import com.netcracker.coctail.model.UserPasswords;
import com.netcracker.coctail.model.UserPersonalInfo;

/**
 * Service interface for class {User}.
 */

public interface UserService {

    User getUserByEmail(String email);

    User getUserById(Long id);

    String getRolenameByEmail(String email);

    String changeUserPassword(User user, String password);

    void checkPassword(User user, UserPasswords userPasswords);

    void changeInfo(String email, UserPersonalInfo user);
}
