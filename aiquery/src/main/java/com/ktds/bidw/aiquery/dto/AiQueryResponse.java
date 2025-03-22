package com.ktds.bidw.aiquery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * AI 질의응답 응답 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "AI 질의응답 응답")
public class AiQueryResponse {

    @Schema(description = "AI 응답 내용", example = "이 리포트에서 가장 높은 매출이 발생한 월은 5월입니다.")
    private String answer;

    @Schema(description = "대화 컨텍스트 ID", example = "conv-123456")
    private String conversationId;

    @Schema(description = "참조 정보")
    private List<String> references;

    @Schema(description = "응답 관련 데이터")
    private List<Map<String, Object>> data;
}
