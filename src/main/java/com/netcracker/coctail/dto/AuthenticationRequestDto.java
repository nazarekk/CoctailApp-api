package com.netcracker.coctail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO class for authentication (login) request.
 */

@Data
@AllArgsConstructor
public class AuthenticationRequestDto {
    private String email;
    private String password;
}
