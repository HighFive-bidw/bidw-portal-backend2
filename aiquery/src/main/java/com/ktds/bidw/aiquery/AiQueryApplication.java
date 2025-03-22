package com.ktds.bidw.aiquery;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * AI 질의응답 서비스의 메인 애플리케이션 클래스입니다.
 */
@SpringBootApplication
public class AiQueryApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiQueryApplication.class, args);
    }
}