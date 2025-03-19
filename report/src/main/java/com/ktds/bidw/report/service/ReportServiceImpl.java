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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;

/**
 * 리포트 관련 서비스 구현 클래스입니다.
 * 리포트 다운로드 성공률 관련 메트릭을 수집합니다.
 */
@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ExcelService excelService;

    // 메트릭
    private final Timer reportDownloadTimer;
    private final Counter reportDownloadTotalCounter;
    private final Counter reportDownloadSuccessCounter;
    private final Counter reportDownloadFailureCounter;

    /**
     * 생성자를 통해 의존성을 주입받습니다.
     */
    @Autowired
    public ReportServiceImpl(
            ReportRepository reportRepository,
            ExcelService excelService,
            @Qualifier("reportDownloadTimer") Timer reportDownloadTimer,
            @Qualifier("reportDownloadTotalCounter") Counter reportDownloadTotalCounter,
            @Qualifier("reportDownloadSuccessCounter") Counter reportDownloadSuccessCounter,
            @Qualifier("reportDownloadFailureCounter") Counter reportDownloadFailureCounter) {
        this.reportRepository = reportRepository;
        this.excelService = excelService;
        this.reportDownloadTimer = reportDownloadTimer;
        this.reportDownloadTotalCounter = reportDownloadTotalCounter;
        this.reportDownloadSuccessCounter = reportDownloadSuccessCounter;
        this.reportDownloadFailureCounter = reportDownloadFailureCounter;
    }

    /**
     * 모든 리포트 목록을 조회합니다.
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
     * 다운로드 요청을 측정하고 성공/실패를 계수합니다.
     */
    @Override
    @Transactional(readOnly = true)
    public DownloadResponse downloadReport(Long reportId) {
        // 다운로드 요청 수 증가 - 먼저 증가시켜 정확한 요청 수 측정
        reportDownloadTotalCounter.increment();

        // 서비스 계층 다운로드 처리 시간 측정
        return reportDownloadTimer.record(() -> {
            try {
                Report report = reportRepository.findById(reportId)
                        .orElseThrow(() -> new ReportNotFoundException("리포트를 찾을 수 없습니다: " + reportId));

                ReportData reportData = report.getData();
                String reportName = report.getName();

                ExcelResult excelResult = excelService.generateExcel(reportData, reportName);

                // 현재 시간 + 30분을 URL 만료 시간으로 설정
                LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(30);

                // 다운로드 성공 수 증가
                reportDownloadSuccessCounter.increment();

                return new DownloadResponse(
                        excelResult.getFileUrl(),
                        excelResult.getFileName(),
                        expiryTime
                );
            } catch (Exception e) {
                // 다운로드 실패 수 증가
                reportDownloadFailureCounter.increment();
                log.error("리포트 다운로드 처리 중 오류 발생: {}", e.getMessage(), e);
                throw e;
            }
        });
    }

    /**
     * Report를 ReportListDTO로 변환합니다.
     */
    private ReportListDTO convertToReportListDTO(Report report) {
        return new ReportListDTO(
                report.getId(),
                report.getName(),
                report.getLastUpdated()
        );
    }
}
