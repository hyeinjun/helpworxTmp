package com.benit.helpworx.config;

import com.benit.helpworx.common.interceptor.SecurityInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SecurityInterceptor securityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(securityInterceptor)
                .addPathPatterns("/**")
        .excludePathPatterns("/login"
                ,"/sys/**"
                ,"/jobgb"
                ,"/jobGb/**"
                ,"/conm"
                ,"/usr-info"
                ,"/v2/api-docs"
                ,"/swagger-resources/**"
                ,"/swagger-ui.html"
                ,"/webjars/**"
                );

    }
}
