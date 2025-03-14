package com.ktds.bidw.subscription.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 구독 응답 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "구독 응답")
public class SubscriptionResponse {
    
    @Schema(description = "결과 메시지", example = "리포트가 성공적으로 구독되었습니다.")
    private String message;
    
    @Schema(description = "구독 ID", example = "1")
    private Long subscriptionId;
}
