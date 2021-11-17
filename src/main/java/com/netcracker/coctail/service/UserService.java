package com.netcracker.coctail.service;

import com.netcracker.coctail.model.Role;
import com.netcracker.coctail.model.User;

import java.util.List;

/**
 * Service interface for class {User}.
 */

public interface UserService {

    User getUserByEmail(String email);

    List<Role> getRolesByEmail(String email);

    User getUserById(Long id);

}
