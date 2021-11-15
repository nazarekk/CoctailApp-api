package com.netcracker.coctail.security.jwt;

import org.springframework.security.core.AuthenticationException;

/**
 * Authentication exception for CocktailApp application.
 */

public class JwtAuthenticationException extends AuthenticationException {
    public JwtAuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }

    public JwtAuthenticationException(String msg) {
        super(msg);
    }
}
