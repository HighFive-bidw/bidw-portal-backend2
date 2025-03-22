package com.ktds.bidw.aiquery.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Swagger 설정 클래스입니다.
 */
@Configuration
public class SwaggerConfig {

    @Value("${server.port:8083}")
    private String serverPort;

    /**
     * Swagger OpenAPI 구성을 제공합니다.
     *
     * @return OpenAPI 인스턴스
     */
    @Bean
    public OpenAPI openAPI() {
        String securitySchemeName = "bearerAuth";

        // 서버 정보 설정
        Server localServer = new Server();
        localServer.setUrl("http://localhost:" + serverPort);
        localServer.setDescription("로컬 서버");

        return new OpenAPI()
                .info(new Info()
                        .title("AI 질의응답 서비스 API")
                        .description("BIDW 포털의 AI 질의응답 서비스 API 문서입니다.<br>" +
                                "리포트 데이터에 대한 질의응답과 대화 히스토리 관리를 제공합니다.<br><br>" +
                                "<b>주요 기능:</b><br>" +
                                "- 리포트 데이터 기반 AI 질의응답<br>" +
                                "- 대화 컨텍스트 관리<br>" +
                                "- 대화 히스토리 저장 및 조회<br>" +
                                "- 리포트 유형별 추천 질문 제공")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("BIDW 개발팀")
                                .email("bidw-dev@ktds.com")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .servers(Arrays.asList(localServer));
    }
}