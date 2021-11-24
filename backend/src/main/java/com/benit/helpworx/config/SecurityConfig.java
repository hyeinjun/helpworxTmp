package com.benit.helpworx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Override
//    @Bean
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
    @Override
    public void configure(WebSecurity web){
        web.ignoring().antMatchers("/login"
                ,"/sys/**"
                ,"/jobgb"
                ,"/jobGb/**"
                ,"/conm"
                ,"/usr-info"
                ,"/v2/api-docs"
                ,"/swagger-resources/**"
                ,"/swagger-ui.html"
                ,"/webjars/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.
                httpBasic().disable()//rest api만 고려, 기본설정 해제
                .csrf().disable()//csrf disable
                .formLogin().disable()//폼 기반 인증 disable
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//토큰 인증 - 세션 사용x
                .and()
                .authorizeRequests()
                .anyRequest().permitAll()
                ;
    }

}
