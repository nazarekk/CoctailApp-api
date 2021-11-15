package com.netcracker.coctail.security.jwt;

import com.netcracker.coctail.model.Role;
import com.netcracker.coctail.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of Factory Method for class {JwtUser}.
 */

public final class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getNickname(),
                user.getPassword(),
                user.getEmail(),
                user.getRoleid(),
                user.isIsactive(),
                mapToGrantedAuthorities(new ArrayList<Role>())
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> userRoles) {
        return userRoles.stream()
                .map(role ->
                        new SimpleGrantedAuthority(role.getRolename())
                ).collect(Collectors.toList());
    }
}
