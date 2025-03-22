package com.ktds.bidw.aiquery.service.impl;

import com.ktds.bidw.aiquery.dto.ReportDetailResponse;
import com.ktds.bidw.aiquery.service.ReportClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 리포트 서비스 클라이언트 간소화 구현 클래스입니다.
 */
@Slf4j
@Service
public class ReportClientImpl implements ReportClient {

    private final RestTemplate restTemplate;
    private final String reportServiceUrl;

    /**
     * 생성자를 통해 RestTemplate과 리포트 서비스 URL을 주입받습니다.
     */
    public ReportClientImpl(
            RestTemplate restTemplate,
            @Value("${service.report.url:http://localhost:8081}") String reportServiceUrl) {
        this.restTemplate = restTemplate;
        this.reportServiceUrl = reportServiceUrl;
        log.info("Report Client initialized with URL: {}", reportServiceUrl);
    }

    /**
     * 리포트 상세 정보를 조회합니다.
     * 실제 서비스 호출이 실패할 경우 샘플 데이터를 반환합니다.
     */
    @Override
    public ReportDetailResponse getReportDetail(Long reportId) {
        try {
            String url = reportServiceUrl + "/api/reports/" + reportId;
            log.debug("Calling report service: {}", url);

            return restTemplate.getForObject(url, ReportDetailResponse.class);

        } catch (RestClientException e) {
            log.error("Error calling report service: {}", e.getMessage());
            // 샘플 데이터 반환
            return createSampleReportDetail(reportId);
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e);
            return createSampleReportDetail(reportId);
        }
    }

    /**
     * 샘플 리포트 데이터 생성
     */
    private ReportDetailResponse createSampleReportDetail(Long reportId) {
        String reportName = "샘플 리포트 #" + reportId;
        LocalDateTime lastUpdated = LocalDateTime.now();

        // 샘플 데이터 생성
        List<Map<String, Object>> sampleData = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", i);
            row.put("category", "카테고리 " + i);
            row.put("value", i * 100);
            row.put("date", LocalDateTime.now().minusDays(i));

            sampleData.add(row);
        }

        return new ReportDetailResponse(reportId, reportName, lastUpdated, sampleData);
    }
}