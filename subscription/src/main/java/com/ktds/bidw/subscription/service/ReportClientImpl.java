package com.ktds.bidw.subscription.service;

import com.ktds.bidw.subscription.domain.ReportInfo;
import com.ktds.bidw.subscription.exception.SubscriptionNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 리포트 서비스 클라이언트 구현 클래스입니다.
 */
@Slf4j
@Service
public class ReportClientImpl implements ReportClient {

    private final RestTemplate restTemplate;
    private final String reportServiceUrl;
    
    /**
     * 생성자를 통해 RestTemplate과 리포트 서비스 URL을 주입받습니다.
     *
     * @param restTemplate RestTemplate 객체
     * @param reportServiceUrl 리포트 서비스 URL
     */
    public ReportClientImpl(RestTemplate restTemplate, @Value("${service.report.url}") String reportServiceUrl) {
        this.restTemplate = restTemplate;
        this.reportServiceUrl = reportServiceUrl;
    }
    
    /**
     * 리포트 정보를 조회합니다.
     *
     * @param reportId 리포트 ID
     * @return 리포트 정보
     */
    @Override
    public ReportInfo getReportInfo(Long reportId) {
        String url = UriComponentsBuilder.fromHttpUrl(reportServiceUrl)
                .path("/api/reports/{reportId}")
                .buildAndExpand(reportId)
                .toUriString();
        
        try {
            return restTemplate.getForObject(url, ReportInfo.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new SubscriptionNotFoundException("리포트를 찾을 수 없습니다: " + reportId);
            }
            log.error("리포트 서비스 호출 중 오류가 발생했습니다", e);
            throw new RuntimeException("리포트 서비스 호출에 실패했습니다: " + e.getMessage());
        }
    }
}
