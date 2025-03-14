package com.ktds.bidw.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 로그인 응답 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "로그인 응답")
public class LoginResponse {
    
    @Schema(description = "인증 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @Schema(description = "사용자 역할", example = "USER")
    private String userRole;
    
    @Schema(description = "토큰 만료 시간", example = "2023-06-01T12:00:00")
    private LocalDateTime expiryTime;
}
