package com.netcracker.coctail.controllers;

import com.netcracker.coctail.dao.ForgotPasswordDao;
import com.netcracker.coctail.dto.AuthenticationRequestDto;
import com.netcracker.coctail.model.User;
import com.netcracker.coctail.security.jwt.JwtTokenProvider;
import com.netcracker.coctail.service.UserService;
import com.netcracker.coctail.services.AuthService;

import javax.servlet.http.HttpServletRequest;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for authentication requests (login, logout, register, etc.)
 */

@RestController
@RequestMapping(value = "/api/auth/")
@CrossOrigin(origins = "*")
@Data
public class AuthenticationRestController {

    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;
    private UserService userService;
    private ForgotPasswordDao forgotPasswordDao;
    private PasswordEncoder passwordEncoder;
    private AuthService authService;

    @Autowired
    AuthenticationRestController(AuthenticationManager authenticationManager,
                                 JwtTokenProvider jwtTokenProvider,
                                 UserService userService,
                                 ForgotPasswordDao forgotPasswordDao,
                                 PasswordEncoder passwordEncoder,
                                 AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.forgotPasswordDao = forgotPasswordDao;
        this.authService = authService;
    }


    @PostMapping(value = "login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> json) {
        AuthenticationRequestDto requestDto = new AuthenticationRequestDto(json.get("email"), json.get("password"));
        String captcha = json.get("captcha");
        return ResponseEntity.ok(authService.loginAuthorization(requestDto, captcha));
    }

    @PostMapping("refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        return new ResponseEntity<>(authService.refreshTokenAuth(request), HttpStatus.OK);
    }


    @PostMapping("restore-password")
    public String confirmUser(@RequestBody Map<String, String> emailMap) {
        User user = userService.getUserByEmail(emailMap.get("email"));
        return forgotPasswordDao.sendCode(user);
    }

    @PostMapping("restore-password/change-password/{code}")
    public String changePasswordUser(@RequestBody Map<String, String> passwordMap,
                                     @PathVariable String code) {
        User user = userService.getUserByEmail(forgotPasswordDao.findByActivationCode(code).getEmail());
        return userService.changeUserPassword(user, passwordMap.get("password"));
    }
}
