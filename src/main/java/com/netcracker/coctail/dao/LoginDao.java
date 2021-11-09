package com.netcracker.coctail.dao;

import com.netcracker.coctail.model.Login;

import java.util.Collection;

public interface LoginDao {
    Login read (String id);
    //void update (Login login);

    /*
            @Override
            public void update(Login login) {
                logins.put(login.getId(), login);
            }
        */
    boolean getByEmailAndPassword(String email,String password);
    Collection<Login> getAll();


}
