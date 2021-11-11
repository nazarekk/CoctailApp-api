package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.ReadUser;
import com.netcracker.coctail.model.CreateUser;

import java.util.Collection;

public interface PostgresRegistrationDao {
    String create(CreateUser user);
    Collection<ReadUser> getAll();
}