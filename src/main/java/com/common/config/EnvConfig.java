package com.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
public class EnvConfig {
    
    @Value("${spring.profiles.active}")
    private String activeProfile;
    
    @PostConstruct
    public void init() {
        log.info("当前运行环境: {}", activeProfile);
    }
    
    public boolean isDev() {
        return "dev".equals(activeProfile);
    }
    
    public boolean isTest() {
        return "test".equals(activeProfile);
    }
    
    public boolean isProd() {
        return "prod".equals(activeProfile);
    }
}