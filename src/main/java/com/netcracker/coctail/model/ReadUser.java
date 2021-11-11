package com.netcracker.coctail.model;

import lombok.Data;

@Data
public class ReadUser {
    int id;
    String email;
    String password;

    public ReadUser(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }
}
