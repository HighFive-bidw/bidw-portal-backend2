package com.ktds.bidw.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 리포트 상세 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "리포트 상세 정보")
public class ReportDetailDTO {
    
    @Schema(description = "리포트 ID", example = "1")
    private Long reportId;
    
    @Schema(description = "리포트 이름", example = "월간 매출 리포트")
    private String reportName;
    
    @Schema(description = "마지막 업데이트 일시", example = "2023-06-01T12:00:00")
    private LocalDateTime lastUpdated;
    
    @Schema(description = "리포트 데이터", example = "[{\"id\":1,\"name\":\"상품A\",\"value\":1000},{\"id\":2,\"name\":\"상품B\",\"value\":2000}]")
    private List<Map<String, Object>> data;
}
