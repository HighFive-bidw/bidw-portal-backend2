package com.ktds.bidw.subscription.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * REST 클라이언트 설정 클래스입니다.
 */
@Configuration
public class RestClientConfig {

    /**
     * RestTemplate 빈을 생성합니다.
     *
     * @return RestTemplate 인스턴스
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
