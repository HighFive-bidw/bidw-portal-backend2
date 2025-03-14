package com.ktds.bidw.report.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 리포트 데이터 도메인 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportData {
    
    private List<String> headers;
    private List<Map<String, Object>> rows;
}
