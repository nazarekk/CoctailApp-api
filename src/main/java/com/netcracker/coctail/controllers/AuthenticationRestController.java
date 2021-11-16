package com.netcracker.coctail.controllers;

import com.netcracker.coctail.dto.AuthenticationRequestDto;
import com.netcracker.coctail.security.jwt.JwtTokenProvider;
import com.netcracker.coctail.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for authentication requests (login, logout, register, etc.)
 */

@RestController
@RequestMapping(value = "/api/auth/")
@CrossOrigin(origins = "${front_link}")
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    @Autowired
    public AuthenticationRestController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("login")
    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto) {
            String email = requestDto.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, requestDto.getPassword()));
            userService.getUserByEmail(email);

            String token = jwtTokenProvider.createToken(email, userService.getRolesByEmail(email));

            Map<Object, Object> response = new HashMap<>();
            response.put("email", email);
            response.put("token", token);

            return ResponseEntity.ok(response);
    }
}
