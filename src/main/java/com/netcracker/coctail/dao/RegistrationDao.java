package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.CreateUser;

public interface RegistrationDao {
    String create(CreateUser user);
    int activateUser(String code);
}