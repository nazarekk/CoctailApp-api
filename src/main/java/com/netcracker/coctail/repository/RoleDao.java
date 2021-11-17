package com.netcracker.coctail.repository;

import com.netcracker.coctail.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface that extends {@link JpaRepository} for class {@link Role}.
 */

public interface RoleDao {
    List<Role> findRoleNameByEmail(String email);
}
