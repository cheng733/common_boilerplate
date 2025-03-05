package com.common.config;

import com.common.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Autowired private JwtInterceptor jwtInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry
        .addInterceptor(jwtInterceptor)
        .addPathPatterns("/api/**")
        .excludePathPatterns("/api/auth/**")
        .excludePathPatterns("/api/user/register")
        .excludePathPatterns("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs");
  }
}
