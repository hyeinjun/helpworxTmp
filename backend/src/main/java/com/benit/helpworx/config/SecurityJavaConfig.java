package com.benit.helpworx.config;

import com.benit.helpworx.common.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityJavaConfig {

    @Value("${jwt.access}")
    private String accessSecret;

    @Value("${jwt.refresh}")
    private String refreshSecret;

    @Bean
    public JwtTokenUtil jwtTokenUtil(){
        return new JwtTokenUtil(accessSecret,refreshSecret);
    }
}
