package com.david.bookmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * CORS跨域配置类
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 允许所有源
        configuration.addAllowedOriginPattern("*");
        
        // 允许的HTTP方法
        configuration.addAllowedMethod("*");
        
        // 允许的请求头
        configuration.addAllowedHeader("*");
        
        // 允许的响应头
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("Content-Type");
        
        // 是否允许发送Cookie
        configuration.setAllowCredentials(true);
        
        // 预检请求的有效期
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
} 