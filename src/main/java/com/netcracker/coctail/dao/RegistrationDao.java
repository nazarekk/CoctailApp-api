package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.ActivateUser;
import com.netcracker.coctail.model.ReadUser;
import com.netcracker.coctail.model.CreateUser;

import java.util.Collection;

public interface RegistrationDao {
    String create(CreateUser user);
    boolean activateUser(String code);
    public Collection<ReadUser> getByCode(String code);
}