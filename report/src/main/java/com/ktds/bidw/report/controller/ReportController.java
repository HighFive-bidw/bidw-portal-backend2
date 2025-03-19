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
    private final Timer downloadTimer;
    private final Counter downloadCounter;

    /**
     * 생성자를 통해 의존성 및 메트릭을 초기화합니다.
     *
     * @param reportService 리포트 서비스
     * @param meterRegistry 메트릭 레지스트리
     */
    public ReportController(ReportService reportService, MeterRegistry meterRegistry) {
        this.reportService = reportService;

        // 다운로드 처리 시간 측정을 위한 타이머 메트릭 생성
        this.downloadTimer = Timer.builder("http_report_download_seconds")
                .description("리포트 다운로드 요청 처리 시간")
                .tag("outcome", "success")
                .publishPercentiles(0.5, 0.95, 0.99) // Grafana 대시보드에서 사용하는 백분위수
                .register(meterRegistry);

        // 다운로드 요청 수 집계를 위한 카운터 메트릭 생성
        this.downloadCounter = Counter.builder("report_download_total")
                .description("총 리포트 다운로드 요청 수")
                .register(meterRegistry);
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
     * 다운로드 요청 시간을 측정하고 요청 횟수를 집계합니다.
     *
     * @param reportId 리포트 ID
     * @return 다운로드 정보
     */
    @GetMapping("/{reportId}/download")
    @Operation(summary = "리포트 다운로드", description = "특정 리포트를 Excel 형식으로 다운로드합니다.")
    public ResponseEntity<DownloadResponse> downloadReport(
            @Parameter(description = "리포트 ID", required = true)
            @PathVariable Long reportId) {

        // 타이머로 다운로드 처리 시간 측정
        DownloadResponse response = downloadTimer.record(() -> {
            try {
                DownloadResponse result = reportService.downloadReport(reportId);
                // 성공적인 다운로드 요청 횟수 증가
                downloadCounter.increment();
                return result;
            } catch (Exception e) {
                log.error("리포트 다운로드 중 오류 발생: {}", e.getMessage(), e);
                throw e;
            }
        });

        return ResponseEntity.ok(response);
    }
}