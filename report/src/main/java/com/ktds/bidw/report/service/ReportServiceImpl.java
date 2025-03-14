package com.ktds.bidw.report.service;

import com.ktds.bidw.common.util.ValidationUtils;
import com.ktds.bidw.report.domain.Report;
import com.ktds.bidw.report.domain.ReportData;
import com.ktds.bidw.report.dto.DownloadResponse;
import com.ktds.bidw.report.dto.ExcelResult;
import com.ktds.bidw.report.dto.ReportDetailDTO;
import com.ktds.bidw.report.dto.ReportListDTO;
import com.ktds.bidw.report.exception.ReportNotFoundException;
import com.ktds.bidw.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 리포트 관련 서비스 구현 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ExcelService excelService;
    
    /**
     * 모든 리포트 목록을 조회합니다.
     *
     * @return 리포트 목록
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReportListDTO> getReportList() {
        return reportRepository.findAll().stream()
                .map(this::convertToReportListDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 날짜로 필터링된 리포트 목록을 조회합니다.
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 필터링된 리포트 목록
     */
    @Override
    @Transactional(readOnly = true)
    public List<ReportListDTO> getFilteredReports(LocalDate startDate, LocalDate endDate) {
        ValidationUtils.validateStartEndDates(startDate, endDate);
        
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
        
        return reportRepository.findByLastUpdatedBetween(startDateTime, endDateTime).stream()
                .map(this::convertToReportListDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 특정 리포트의 상세 정보를 조회합니다.
     *
     * @param reportId 리포트 ID
     * @return 리포트 상세 정보
     */
    @Override
    @Transactional(readOnly = true)
    public ReportDetailDTO getReportDetail(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportNotFoundException("리포트를 찾을 수 없습니다: " + reportId));
        
        return new ReportDetailDTO(
                report.getId(),
                report.getName(),
                report.getLastUpdated(),
                report.getData().getRows()
        );
    }
    
    /**
     * 특정 리포트를 Excel 형식으로 다운로드합니다.
     *
     * @param reportId 리포트 ID
     * @return 다운로드 정보
     */
    @Override
    @Transactional(readOnly = true)
    public DownloadResponse downloadReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportNotFoundException("리포트를 찾을 수 없습니다: " + reportId));
        
        ReportData reportData = report.getData();
        String reportName = report.getName();
        
        ExcelResult excelResult = excelService.generateExcel(reportData, reportName);
        
        // 현재 시간 + 30분을 URL 만료 시간으로 설정
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(30);
        
        return new DownloadResponse(
                excelResult.getFileUrl(),
                excelResult.getFileName(),
                expiryTime
        );
    }
    
    /**
     * Report를 ReportListDTO로 변환합니다.
     *
     * @param report Report 객체
     * @return ReportListDTO 객체
     */
    private ReportListDTO convertToReportListDTO(Report report) {
        return new ReportListDTO(
                report.getId(),
                report.getName(),
                report.getLastUpdated()
        );
    }
}
