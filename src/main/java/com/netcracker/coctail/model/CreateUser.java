package com.netcracker.coctail.model;

import lombok.Data;


@Data
public class CreateUser {
    private final String email;
    private final String password;
    private final String activation;
}
