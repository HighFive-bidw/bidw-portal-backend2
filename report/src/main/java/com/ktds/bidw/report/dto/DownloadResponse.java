package com.ktds.bidw.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 다운로드 응답 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "다운로드 응답 정보")
public class DownloadResponse {
    
    @Schema(description = "다운로드 URL", example = "https://storage.example.com/reports/report_1.xlsx?token=abc123")
    private String downloadUrl;
    
    @Schema(description = "파일 이름", example = "report_1.xlsx")
    private String fileName;
    
    @Schema(description = "URL 만료 시간", example = "2023-06-01T12:30:00")
    private LocalDateTime expiryTime;
}
