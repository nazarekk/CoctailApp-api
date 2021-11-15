package com.netcracker.coctail.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Simple domain object that represents application user's role - ADMIN, USER, etc.
 */

@Data
@AllArgsConstructor
public class Role {

    private Long id;
    private String rolename;


}
