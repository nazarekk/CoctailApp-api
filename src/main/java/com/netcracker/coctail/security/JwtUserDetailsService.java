package com.netcracker.coctail.security;

import com.netcracker.coctail.model.User;
import com.netcracker.coctail.security.jwt.JwtUserFactory;
import com.netcracker.coctail.service.UserService;
import lombok.extern.slf4j.Slf4j;
import com.netcracker.coctail.security.jwt.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link UserDetailsService} interface for {@link JwtUser}.
 */

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userService.getUserByEmail(email);

        JwtUser jwtUser = JwtUserFactory.create(user);
        log.info("IN loadUserByUsername - user with username: {} successfully loaded", email);
        return jwtUser;
    }
}
