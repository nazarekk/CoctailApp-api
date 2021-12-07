package com.netcracker.coctail.controllers;

import com.netcracker.coctail.dao.ForgotPasswordDao;
import com.netcracker.coctail.dto.AuthenticationRequestDto;
import com.netcracker.coctail.exceptions.InvalidEmailOrPasswordException;
import com.netcracker.coctail.model.User;
import com.netcracker.coctail.security.jwt.JwtTokenProvider;
import com.netcracker.coctail.service.UserService;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

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


    @PostMapping("login")
    public ResponseEntity<?>login(@RequestBody AuthenticationRequestDto requestDto) {
        String email = requestDto.getEmail();
        User user = userService.getUserByEmail(email);
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new InvalidEmailOrPasswordException();
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, requestDto.getPassword()));
        String role = userService.getRolenameByEmail(email);
        String accessToken = jwtTokenProvider.createToken(email, role);

        Map<Object, Object> response = new HashMap<>();
        response.put("role", role);
        response.put("token", accessToken);

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "refreshtoken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) throws Exception {
        System.out.println(request.getAttribute("claims"));
        DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");
        Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
        String refreshToken = jwtTokenProvider.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
        String email = expectedMap.get("sub").toString();
        String role = expectedMap.get("role").toString();
        String accessToken = jwtTokenProvider.createToken(email, role);
        Map<Object, Object> response = new HashMap<>();
        response.put("token", accessToken);
        response.put("refreshToken", refreshToken);
        return new ResponseEntity(response,HttpStatus.OK);
    }

    public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
        Map<String, Object> expectedMap = new HashMap<>();
        for (Entry<String, Object> entry : claims.entrySet()) {
            expectedMap.put(entry.getKey(), entry.getValue());
        }
        return expectedMap;
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
