package com.ktds.bidw.aiquery.service;

import com.ktds.bidw.aiquery.dto.ReportDetailResponse;

/**
 * 리포트 서비스 클라이언트 인터페이스입니다.
 */
public interface ReportClient {

    /**
     * 리포트 상세 정보를 조회합니다.
     *
     * @param reportId 리포트 ID
     * @return 리포트 상세 정보
     */
    ReportDetailResponse getReportDetail(Long reportId);
}