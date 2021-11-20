package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface that extends {@link JpaRepository} for class {@link User}.
 */

public interface UserDao {
    List<User> findUserByEmail(String email);
    List<User> findUserById(Long id);
}
