package com.netcracker.coctail.dto;

import lombok.Data;

/**
 * DTO class for authentication (login) request.
 */

@Data
public class AuthenticationRequestDto {
    private String email;
    private String password;
}
