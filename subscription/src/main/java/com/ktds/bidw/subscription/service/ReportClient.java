package com.ktds.bidw.subscription.service;

import com.ktds.bidw.subscription.domain.ReportInfo;

/**
 * 리포트 서비스 클라이언트 인터페이스입니다.
 */
public interface ReportClient {
    
    /**
     * 리포트 정보를 조회합니다.
     *
     * @param reportId 리포트 ID
     * @return 리포트 정보
     */
    ReportInfo getReportInfo(Long reportId);
}
