package com.netcracker.coctail.security.jwt;

import com.netcracker.coctail.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;


/**
 * Implementation of Factory Method for class {JwtUser}.
 */

public final class JwtUserFactory {

    public static JwtUser create(User user, String roleName) {
        return new JwtUser(
                user.getId(),
                user.getNickname(),
                user.getPassword(),
                user.getEmail(),
                user.getRoleid(),
                user.isIsactive(),
                Collections.singletonList(new SimpleGrantedAuthority(roleName))
        );
    }
}

