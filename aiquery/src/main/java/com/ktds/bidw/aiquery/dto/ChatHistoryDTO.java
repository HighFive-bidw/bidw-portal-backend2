package com.ktds.bidw.aiquery.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 대화 히스토리 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "대화 히스토리 정보")
public class ChatHistoryDTO {

    @Schema(description = "히스토리 ID", example = "1")
    private Long id;

    @Schema(description = "리포트 ID", example = "1")
    private Long reportId;

    @Schema(description = "리포트 이름", example = "월간 매출 리포트")
    private String reportName;

    @Schema(description = "질문 내용", example = "이 리포트에서 가장 높은 매출이 발생한 월은?")
    private String question;

    @Schema(description = "AI 응답 내용", example = "이 리포트에서 가장 높은 매출이 발생한 월은 5월입니다.")
    private String answer;

    @Schema(description = "생성 일시", example = "2023-06-01T12:00:00")
    private LocalDateTime createdAt;
}