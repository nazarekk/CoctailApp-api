package com.netcracker.coctail.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data @AllArgsConstructor
public class Users {
    int id;
    String email;
    String password;

    public Users(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
