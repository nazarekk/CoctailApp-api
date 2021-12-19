package com.netcracker.coctail.config;

import com.netcracker.coctail.security.jwt.JwtAuthenticationEntryPoint;
import com.netcracker.coctail.security.jwt.JwtConfigurer;
import com.netcracker.coctail.security.jwt.JwtTokenProvider;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Security configuration class for JWT based Spring Security application.
 */

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

  private final JwtTokenProvider jwtTokenProvider;

  private static final String ADMIN_ENDPOINT = "/api/admin/**";
  private static final String MODERATOR_ENDPOINT = "/api/moderator/**";
  private static final String MODERACTIVATION_ENDPOINT = "/api/moderator/activation**";
  private static final String USERACTIVATION_ENDPOINT = "/api/users/activation**";
  private static final String AUTHENTICAL_ENDPOINT = "/api/auth/**";
  private static final String REFRESH_ENDPOINT = "/api/auth/refresh-token";
  private static final String USER_ENDPOINT = "/api/users**";
  private static final String REG_ENDPOINT = "/api/users";
  private static final String DISHES_ENDPOINT = "/api/users/recipe/**";
  private static final String front_link = "${front_link}";

  @Autowired
  public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
    this.jwtTokenProvider = jwtTokenProvider;
  }

  @Autowired
  private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  @Bean
  public RestTemplate getRestTemplate() {
    return new RestTemplate();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
        .cors().configurationSource(corsConfigurationSource())
        .and()
        .authorizeRequests()
        .antMatchers(AUTHENTICAL_ENDPOINT).permitAll()
        .antMatchers(REG_ENDPOINT).permitAll()
        .antMatchers(MODERACTIVATION_ENDPOINT).permitAll()
        .antMatchers(USERACTIVATION_ENDPOINT).permitAll()
        .antMatchers(REFRESH_ENDPOINT).permitAll()
        .antMatchers(DISHES_ENDPOINT).permitAll()
        .antMatchers(ADMIN_ENDPOINT).hasRole("ADMIN")
        .antMatchers(MODERATOR_ENDPOINT).hasRole("MODERATOR")
        .antMatchers(USER_ENDPOINT).hasRole("CONFIRMED")
        .antMatchers(USER_ENDPOINT).hasRole("MODERATOR")
        .anyRequest().authenticated()
        .and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().apply(new JwtConfigurer(jwtTokenProvider));

  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("*"));
    configuration.setAllowedMethods(Arrays.asList("*"));
    configuration.addAllowedOrigin(front_link);
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
