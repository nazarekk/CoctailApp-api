package com.netcracker.coctail.model;

import lombok.Data;

@Data
public class ReadUser {
    private final long userid;
    private final String email;
    private final long roleId;
}
