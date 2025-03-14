package com.ktds.bidw.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Web MVC 설정 클래스입니다.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    
    /**
     * CORS 설정을 구성합니다.
     *
     * @param registry CorsRegistry 객체
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
