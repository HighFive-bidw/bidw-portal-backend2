package com.ktds.bidw.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그인 요청 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "로그인 요청")
public class LoginRequest {
    
    @Schema(description = "사용자 ID", example = "user01")
    private String username;
    
    @Schema(description = "비밀번호", example = "password123")
    private String password;
}
