package com.netcracker.coctail.config;

import com.netcracker.coctail.security.jwt.JwtConfigurer;
import com.netcracker.coctail.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Security configuration class for JWT based Spring Security application.
 */

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String ADMIN_ENDPOINT = "/api/admin/**";
    private static final String MODERATOR_ENDPOINT = "/api/moderators/**";
    //private static final String ALL_ENDPOINT = "/**";
    private static final String AUTHENTICAL_ENDPOINT = "/api/auth/**";
    private static final String REG_ENDPOINT = "/api/users/**";
    //private static final String front_link = "${front_link}";

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(AUTHENTICAL_ENDPOINT).permitAll()
                .antMatchers(REG_ENDPOINT).permitAll()
                .antMatchers(ADMIN_ENDPOINT).permitAll()
                .antMatchers(MODERATOR_ENDPOINT).permitAll()
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*");
    }
}
