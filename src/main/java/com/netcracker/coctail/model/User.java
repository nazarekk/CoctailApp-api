package com.netcracker.coctail.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Simple domain object that represents application user.
 */

@Data
@AllArgsConstructor
public class User {
    private Long id;
    private String nickname;
    private String email;
    private String password;
    private Long roleid;
    private boolean isactive;
    private String image;
}
