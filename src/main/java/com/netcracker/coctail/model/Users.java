package com.netcracker.coctail.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
public class Users {
    int id;
    String email;
    String password;

    public Users(int id,String email, String password) {
        this.id=id;
        this.email = email;
        this.password = password;
    }
}
