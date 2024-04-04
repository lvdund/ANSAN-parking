package com.sparking.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**") // xác định URI
    .allowedOrigins("*") // Chỉ định các domain, * là tất cả
    .allowedMethods("PUT", "DELETE", "GET", "POST", "*") // chỉ định các method, * là tất cả
    .allowedHeaders("*"); // chỉ định các header cho phép
    }
}
