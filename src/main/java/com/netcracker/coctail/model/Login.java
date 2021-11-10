package com.netcracker.coctail.model;

import lombok.Data;

@Data
public class Login {
    private Integer id;
    private String email;
    private String password;

    public Login() {
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Login(Integer id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }
}
