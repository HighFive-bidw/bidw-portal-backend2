package com.ktds.bidw.aiquery.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
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
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 타임아웃 설정 증가
        factory.setConnectTimeout(60000); // 60초
        factory.setReadTimeout(60000); // 60초

        // 프록시 설정이 필요한 경우 여기에 추가
        /*
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxyHost", proxyPort));
        factory.setProxy(proxy);
        */

        return new RestTemplate(factory);
    }

    /**
     * ObjectMapper 빈을 생성합니다.
     *
     * @return ObjectMapper 인스턴스
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // JSR310 모듈 등록 (Java 8 날짜/시간 타입 지원)
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }
}