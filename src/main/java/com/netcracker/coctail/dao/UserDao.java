package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.User;
import com.netcracker.coctail.model.UserInfo;
import com.netcracker.coctail.model.UserPersonalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface that extends {@link JpaRepository} for class {@link User}.
 */

public interface UserDao {
    List<User> findUserByEmail(String email);
    List<User> findUserById(Long id);
    UserInfo myInfo(String email);
    int editInfo(String email, UserPersonalInfo user);
    UserPersonalInfo getInfo(String email);
    List<UserPersonalInfo> findUsersByNickname(String email, UserPersonalInfo user);
}
