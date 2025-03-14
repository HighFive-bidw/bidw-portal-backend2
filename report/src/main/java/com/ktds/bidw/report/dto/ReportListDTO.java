package com.ktds.bidw.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 리포트 목록 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "리포트 목록 정보")
public class ReportListDTO {
    
    @Schema(description = "리포트 ID", example = "1")
    private Long reportId;
    
    @Schema(description = "리포트 이름", example = "월간 매출 리포트")
    private String reportName;
    
    @Schema(description = "마지막 업데이트 일시", example = "2023-06-01T12:00:00")
    private LocalDateTime lastUpdated;
}
