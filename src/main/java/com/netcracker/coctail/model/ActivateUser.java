package com.netcracker.coctail.model;

import lombok.Data;

@Data
public class ActivateUser {
    private final String email;
    private int roleId;
}
