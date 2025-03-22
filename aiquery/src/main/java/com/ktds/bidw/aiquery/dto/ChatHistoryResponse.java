package com.ktds.bidw.aiquery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 대화 히스토리 저장 응답 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "대화 히스토리 저장 응답")
public class ChatHistoryResponse {

    @Schema(description = "결과 메시지", example = "대화 히스토리가 저장되었습니다.")
    private String message;

    @Schema(description = "히스토리 ID", example = "1")
    private Long historyId;
}
