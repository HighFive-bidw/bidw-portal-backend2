package com.ktds.bidw.aiquery.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 리포트 상세 정보 응답 DTO 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDetailResponse {

    private Long reportId;
    private String reportName;
    private LocalDateTime lastUpdated;
    private List<Map<String, Object>> data;
}