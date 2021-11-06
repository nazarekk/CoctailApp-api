package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.Users;

import java.util.Collection;

public interface PostgresRegistrationDao {
    void create(Users user);
    Collection<Users> getAll();
}