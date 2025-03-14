package com.ktds.bidw.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 로그아웃 응답 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "로그아웃 응답")
public class LogoutResponse {
    
    @Schema(description = "결과 메시지", example = "로그아웃 되었습니다.")
    private String message;
}
