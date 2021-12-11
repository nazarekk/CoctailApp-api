package com.netcracker.coctail.controllers;

import com.netcracker.coctail.dao.ForgotPasswordDao;
import com.netcracker.coctail.dto.AuthenticationRequestDto;
import com.netcracker.coctail.model.User;
import com.netcracker.coctail.security.jwt.JwtTokenProvider;
import com.netcracker.coctail.service.UserService;
import com.netcracker.coctail.services.AuthService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * REST controller for authentication requests (login, logout, register, etc.)
 */

@RestController
@RequestMapping(value = "/api/auth/")
@CrossOrigin(origins = "*")
@Data
@AllArgsConstructor
public class AuthenticationRestController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final ForgotPasswordDao forgotPasswordDao;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthService authService;


    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDto requestDto) {
        return ResponseEntity.ok(authService.loginAuthorization(requestDto));
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
    public String changePasswordUser(@RequestBody Map<String, String> passwordMap, @PathVariable String code) {
        User user = userService.getUserByEmail(forgotPasswordDao.findByActivationCode(code).getEmail());
        return userService.changeUserPassword(user, passwordMap.get("password"));
    }
}
