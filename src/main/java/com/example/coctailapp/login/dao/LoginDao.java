package com.example.coctailapp.login.dao;

import com.example.coctailapp.login.model.Login;

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
