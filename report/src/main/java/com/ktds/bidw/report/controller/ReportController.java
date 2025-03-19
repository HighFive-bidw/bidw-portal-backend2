package com.ktds.bidw.report.controller;

import com.ktds.bidw.report.dto.DownloadResponse;
import com.ktds.bidw.report.dto.ReportDetailDTO;
import com.ktds.bidw.report.dto.ReportListDTO;
import com.ktds.bidw.report.service.ReportService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * 리포트 관련 API 컨트롤러입니다.
 */
@Slf4j
@RestController
@RequestMapping("/api/reports")
@Tag(name = "리포트 API", description = "리포트 조회 및 다운로드 API")
public class ReportController {

    private final ReportService reportService;
    private final Timer httpReportDownloadTimer;

    /**
     * 생성자를 통해 의존성 및 메트릭을 초기화합니다.
     *
     * @param reportService 리포트 서비스
     * @param httpReportDownloadTimer HTTP 다운로드 타이머
     */
    public ReportController(
            ReportService reportService,
            Timer httpReportDownloadTimer) {
        this.reportService = reportService;
        this.httpReportDownloadTimer = httpReportDownloadTimer;
    }

    /**
     * 모든 리포트 목록을 조회합니다.
     *
     * @return 리포트 목록
     */
    @GetMapping("/list")
    @Operation(summary = "리포트 목록 조회", description = "사용 가능한 모든 리포트 목록을 조회합니다.")
    public ResponseEntity<List<ReportListDTO>> getReportList() {
        List<ReportListDTO> reports = reportService.getReportList();
        return ResponseEntity.ok(reports);
    }

    /**
     * 날짜로 필터링된 리포트 목록을 조회합니다.
     *
     * @return 필터링된 리포트 목록
     */
    @GetMapping("/filter")
    @Operation(summary = "리포트 필터링", description = "날짜 기준으로 필터링된 리포트 목록을 조회합니다.")
    public ResponseEntity<List<ReportListDTO>> getFilteredReports(
            @RequestParam(name = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(name = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<ReportListDTO> reports = reportService.getFilteredReports(startDate, endDate);
        return ResponseEntity.ok(reports);
    }

    /**
     * 특정 리포트의 상세 정보를 조회합니다.
     *
     * @param reportId 리포트 ID
     * @return 리포트 상세 정보
     */
    @GetMapping("/{reportId}")
    @Operation(summary = "리포트 상세 조회", description = "특정 리포트의 상세 내용을 조회합니다.")
    public ResponseEntity<ReportDetailDTO> getReportDetail(
            @Parameter(description = "리포트 ID", required = true)
            @PathVariable Long reportId) {
        ReportDetailDTO reportDetail = reportService.getReportDetail(reportId);
        return ResponseEntity.ok(reportDetail);
    }

    /**
     * 특정 리포트를 Excel 형식으로 다운로드합니다.
     * HTTP 요청 처리 시간 및 요청 횟수를 측정합니다.
     *
     * @param reportId 리포트 ID
     * @return 다운로드 정보
     */
    @GetMapping("/{reportId}/download")
    @Operation(summary = "리포트 다운로드", description = "특정 리포트를 Excel 형식으로 다운로드합니다.")
    public ResponseEntity<DownloadResponse> downloadReport(
            @Parameter(description = "리포트 ID", required = true)
            @PathVariable Long reportId) {

        // HTTP 계층 타이머로만 측정
        return httpReportDownloadTimer.record(() -> {
            try {
                DownloadResponse response = reportService.downloadReport(reportId);
                return ResponseEntity.ok(response);
            } catch (Exception e) {
                log.error("리포트 다운로드 HTTP 처리 중 오류 발생: {}", e.getMessage(), e);
                throw e;
            }
        });
    }
}