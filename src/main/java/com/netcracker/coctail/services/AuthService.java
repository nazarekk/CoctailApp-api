package com.netcracker.coctail.services;

import com.netcracker.coctail.dto.AuthenticationRequestDto;
import com.netcracker.coctail.exceptions.InvalidEmailOrPasswordException;
import com.netcracker.coctail.model.User;
import com.netcracker.coctail.security.jwt.JwtTokenProvider;
import com.netcracker.coctail.service.UserService;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
                       UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    private final BCryptPasswordEncoder passwordEncoder;

    public Map<Object, Object> loginAuthorization(AuthenticationRequestDto requestDto) {
        String email = requestDto.getEmail();
        User user = userService.getUserByEmail(email);
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new InvalidEmailOrPasswordException();
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, requestDto.getPassword()));
        String role = userService.getRolenameByEmail(email);
        String accessToken = jwtTokenProvider.createToken(email, role);

        Map<Object, Object> response = new HashMap<>();
        response.put("token", accessToken);
        return response;
    }

    public Map<Object, Object> refreshTokenAuth(HttpServletRequest request) {
        DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");
        Map<Object, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
        String email = expectedMap.get("sub").toString();
        String role = expectedMap.get("role").toString();
        String accessToken = jwtTokenProvider.createToken(email, role);
        Map<Object, Object> response = new HashMap<>();
        response.put("token", accessToken);
        return response;
    }

    public Map<Object, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
        Map<Object, Object> expectedMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : claims.entrySet()) {
            expectedMap.put(entry.getKey(), entry.getValue());
        }
        return expectedMap;
    }

}
