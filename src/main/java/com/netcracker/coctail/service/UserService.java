package com.netcracker.coctail.service;

import com.netcracker.coctail.model.User;

import java.util.List;

/**
 * Service interface for class {User}.
 */

public interface UserService {

    List<User> getAll();

    User findByEmail(String email);

    User findById(Long id);

    void delete(Long id);

}
