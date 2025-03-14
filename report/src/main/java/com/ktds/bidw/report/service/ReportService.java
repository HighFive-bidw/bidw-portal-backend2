package com.ktds.bidw.report.service;

import com.ktds.bidw.report.dto.DownloadResponse;
import com.ktds.bidw.report.dto.ReportDetailDTO;
import com.ktds.bidw.report.dto.ReportListDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * 리포트 관련 서비스 인터페이스입니다.
 */
public interface ReportService {
    
    /**
     * 모든 리포트 목록을 조회합니다.
     *
     * @return 리포트 목록
     */
    List<ReportListDTO> getReportList();
    
    /**
     * 날짜로 필터링된 리포트 목록을 조회합니다.
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 필터링된 리포트 목록
     */
    List<ReportListDTO> getFilteredReports(LocalDate startDate, LocalDate endDate);
    
    /**
     * 특정 리포트의 상세 정보를 조회합니다.
     *
     * @param reportId 리포트 ID
     * @return 리포트 상세 정보
     */
    ReportDetailDTO getReportDetail(Long reportId);
    
    /**
     * 특정 리포트를 Excel 형식으로 다운로드합니다.
     *
     * @param reportId 리포트 ID
     * @return 다운로드 정보
     */
    DownloadResponse downloadReport(Long reportId);
}
