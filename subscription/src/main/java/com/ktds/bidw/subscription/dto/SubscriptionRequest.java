package com.ktds.bidw.subscription.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 구독 요청 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "구독 요청")
public class SubscriptionRequest {
    
    @Schema(description = "리포트 ID", example = "2")
    private Long reportId;
}
