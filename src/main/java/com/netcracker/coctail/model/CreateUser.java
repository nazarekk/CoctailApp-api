package com.netcracker.coctail.model;

import lombok.Data;


@Data
public class CreateUser {
    String email;
    String password;

    public CreateUser(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
