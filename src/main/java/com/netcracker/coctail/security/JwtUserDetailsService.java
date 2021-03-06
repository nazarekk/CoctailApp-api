package com.netcracker.coctail.security;

import com.netcracker.coctail.model.User;
import com.netcracker.coctail.security.jwt.JwtUserFactory;
import com.netcracker.coctail.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import com.netcracker.coctail.security.jwt.JwtUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link UserDetailsService} interface for {@link JwtUser}.
 */

@Service
@Slf4j
@Data
public class JwtUserDetailsService implements UserDetailsService {

    private UserService userService;

    @Autowired
    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.getUserByEmail(email);
        String roleName = userService.getRolenameByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User with email: " + email + " not found");
        }
        JwtUser jwtUser = JwtUserFactory.create(user, roleName);
        log.info("IN loadUserByUsername - user with email: {} successfully loaded", email);
        return jwtUser;
    }
}
