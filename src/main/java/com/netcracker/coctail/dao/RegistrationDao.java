package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.ReadUser;
import com.netcracker.coctail.model.CreateUser;
import java.util.Collection;
import java.util.List;

public interface RegistrationDao {
    String create(CreateUser user);
    int activateUser(String code);
}