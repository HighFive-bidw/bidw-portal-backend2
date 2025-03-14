package com.ktds.bidw.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 리포트 필터링 요청 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "리포트 필터링 요청")
public class ReportFilterRequest {
    
    @Schema(description = "시작 날짜", example = "2023-01-01")
    private LocalDate startDate;
    
    @Schema(description = "종료 날짜", example = "2023-01-31")
    private LocalDate endDate;
}
