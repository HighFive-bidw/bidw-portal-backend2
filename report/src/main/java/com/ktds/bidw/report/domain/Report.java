package com.ktds.bidw.report.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 리포트 도메인 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    
    private Long id;
    private String name;
    private String description;
    private LocalDateTime lastUpdated;
    private ReportData data;
}
