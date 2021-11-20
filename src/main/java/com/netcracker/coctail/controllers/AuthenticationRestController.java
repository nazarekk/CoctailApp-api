package com.netcracker.coctail.controllers;

import com.netcracker.coctail.dao.ForgotPasswordDao;
import com.netcracker.coctail.dto.AuthenticationRequestDto;
import com.netcracker.coctail.model.Role;
import com.netcracker.coctail.model.User;
import com.netcracker.coctail.security.jwt.JwtTokenProvider;
import com.netcracker.coctail.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for authentication requests (login, logout, register, etc.)
 */

@RestController
@RequestMapping(value = "/api/auth/")
@CrossOrigin(origins = "*")
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final ForgotPasswordDao forgotPasswordDao;

    @Autowired
    public AuthenticationRestController(AuthenticationManager authenticationManager,
                                        JwtTokenProvider jwtTokenProvider,
                                        UserService userService,
                                        ForgotPasswordDao forgotPasswordDao) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.forgotPasswordDao = forgotPasswordDao;
    }


    @PostMapping("login")
    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto) {
        String email = requestDto.getEmail();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, requestDto.getPassword()));
        List<Role> roles = userService.getRolesByEmail(email);
        String token = jwtTokenProvider.createToken(email, roles);

        Map<Object, Object> response = new HashMap<>();
        response.put("role", roles.get(0).getRolename());
        response.put("token", token);

        return ResponseEntity.ok(response);
    }


    @PostMapping("restore-password")
    public String confirmUser(@RequestBody Map<String, String> emailMap) {
        User user = userService.getUserByEmail(emailMap.get("email"));
        return forgotPasswordDao.sendCode(user);
    }

    @PostMapping("restore-password/change-password/{code}")
    public String changePasswordUser(@RequestBody Map<String, String> passwordMap, @PathVariable String code) {
        User user = userService.getUserByEmail(forgotPasswordDao.findByActivationCode(code.replaceAll("\'", "")).getEmail());
        return userService.changeUserPassword(user, passwordMap.get("password"));
    }
}
