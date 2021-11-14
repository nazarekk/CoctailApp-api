package com.netcracker.coctail.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class WebSecurity {

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
    return bcryptPasswordEncoder;
  }

}
