package com.ktds.bidw.aiquery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * AI 질문 예시 목록 응답 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "AI 질문 예시 목록 응답")
public class AiSuggestionListResponse {

    @Schema(description = "질문 예시 목록")
    private List<String> suggestions;
}