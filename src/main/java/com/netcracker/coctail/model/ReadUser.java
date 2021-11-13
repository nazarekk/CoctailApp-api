package com.netcracker.coctail.model;

import lombok.Data;

@Data
public class ReadUser {
    private final int id;
    private final String email;
    private int roleId;
}
