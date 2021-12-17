package com.netcracker.coctail.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Simple domain object that represents application user.
 */

@Data
@AllArgsConstructor
public class User {
    private final Long id;
    private final String nickname;
    private final String email;
    private final String password;
    private final Long roleid;
    private final boolean isactive;
}
