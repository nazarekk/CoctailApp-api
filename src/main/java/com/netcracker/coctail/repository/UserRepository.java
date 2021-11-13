package com.netcracker.coctail.repository;

import com.netcracker.coctail.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface that extends {@link JpaRepository} for class {@link User}.
 */

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
