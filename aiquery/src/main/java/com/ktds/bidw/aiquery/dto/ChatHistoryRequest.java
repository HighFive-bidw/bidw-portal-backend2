package com.ktds.bidw.aiquery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 대화 히스토리 저장 요청 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "대화 히스토리 저장 요청")
public class ChatHistoryRequest {

    @Schema(description = "리포트 ID", example = "1")
    private Long reportId;

    @Schema(description = "사용자 ID", example = "user01")
    private String userId;

    @Schema(description = "질문 내용", example = "이 리포트에서 가장 높은 매출이 발생한 월은?")
    private String question;

    @Schema(description = "AI 응답 내용", example = "이 리포트에서 가장 높은 매출이 발생한 월은 5월입니다.")
    private String answer;
}