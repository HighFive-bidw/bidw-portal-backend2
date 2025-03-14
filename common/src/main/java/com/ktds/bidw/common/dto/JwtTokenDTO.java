package com.ktds.bidw.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * JWT 토큰 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenDTO {
    
    private String token;
    private LocalDateTime expiryDateTime;
}
