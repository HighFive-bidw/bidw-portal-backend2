package com.ktds.bidw.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 인증 서비스의 메인 애플리케이션 클래스입니다.
 * 
 * @author BIDW 개발팀
 * @version 1.0
 */
@SpringBootApplication
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}
