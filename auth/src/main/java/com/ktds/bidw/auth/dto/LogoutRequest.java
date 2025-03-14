package com.ktds.bidw.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그아웃 요청 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "로그아웃 요청")
public class LogoutRequest {
    
    @Schema(description = "토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
}
