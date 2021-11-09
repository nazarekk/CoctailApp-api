package com.netcracker.coctail.model;

import lombok.Data;


@Data
public class Users {
    String email;
    String password;
    String verification;

    public Users(String email, String password, String verification) {
        this.email = email;
        this.password = password;
        this.verification = verification;
    }
}
