package com.netcracker.coctail.services;

import com.netcracker.coctail.dto.AuthenticationRequestDto;
import com.netcracker.coctail.dto.CaptchaResponseDto;
import com.netcracker.coctail.exceptions.InvalidCaptchaException;
import com.netcracker.coctail.exceptions.InvalidEmailOrPasswordException;
import com.netcracker.coctail.model.User;
import com.netcracker.coctail.security.jwt.JwtTokenProvider;
import com.netcracker.coctail.service.UserService;
import io.jsonwebtoken.impl.DefaultClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Value("${recaptcha.secret}")
    private String secret;

    @Autowired
    private RestTemplate restTemplate;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
                       UserService userService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public Map<Object, Object> loginAuthorization(AuthenticationRequestDto requestDto, String captchaResponse) {
        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
        CaptchaResponseDto captchaResponseDto = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);

        if (captchaResponse == null || !captchaResponseDto.isSuccess()) {
            throw new InvalidCaptchaException();
        }

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
        DefaultClaims claims = (DefaultClaims) request.getAttribute("claims");
        Map<Object, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
        String email = expectedMap.get("sub").toString();
        String role = expectedMap.get("role").toString();
        String accessToken = jwtTokenProvider.createToken(email, role);
        Map<Object, Object> response = new HashMap<>();
        response.put("token", accessToken);
        return response;
    }

    public Map<Object, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
        return new HashMap<>(claims);
    }

}
