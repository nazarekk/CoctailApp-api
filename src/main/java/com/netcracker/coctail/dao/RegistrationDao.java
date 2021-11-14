package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.ReadUser;
import com.netcracker.coctail.model.CreateUser;

import java.util.Collection;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public interface RegistrationDao {
    String create(CreateUser user);
    void activateUser(String code);
    Collection<ReadUser> getByCode(String code);
}