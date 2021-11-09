package com.netcracker.coctail.model;

import lombok.Data;


@Data
public class CreateUser {
    String email;
    String password;
    String verification;

    public CreateUser(String email, String password, String verification) {
        this.email = email;
        this.password = password;
        this.verification = verification;
    }
}
