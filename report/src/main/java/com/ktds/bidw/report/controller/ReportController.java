package com.ktds.bidw.report.controller;

import com.ktds.bidw.report.dto.DownloadResponse;
import com.ktds.bidw.report.dto.ReportDetailDTO;
import com.ktds.bidw.report.dto.ReportListDTO;
import com.ktds.bidw.report.service.ReportService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable; // 경로 변수 사용시
// 타이머 및 카운터 관련 import
import io.micrometer.core.instrument.Counter;




/**
 * 리포트 관련 API 컨트롤러입니다.
 */
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "리포트 API", description = "리포트 조회 및 다운로드 API")
public class ReportController {

    private final ReportService reportService;
    private final MeterRegistry meterRegistry;
    private final Timer downloadTimer;
    private final Counter downloadCounter;
    
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
    /*
    @GetMapping("/filter")
    @Operation(summary = "리포트 필터링", description = "날짜 기준으로 필터링된 리포트 목록을 조회합니다.")
    public ResponseEntity<List<ReportListDTO>> getFilteredReports(@RequestBody ReportFilterRequest request) {
        List<ReportListDTO> reports = reportService.getFilteredReports(request.getStartDate(), request.getEndDate());
        return ResponseEntity.ok(reports);
    }
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
     *
     * @param reportId 리포트 ID
     * @return 다운로드 정보
     */
    @GetMapping("/{reportId}/download")
    @Operation(summary = "리포트 다운로드", description = "특정 리포트를 Excel 형식으로 다운로드합니다.")
    public ResponseEntity<DownloadResponse> downloadReport(
            @Parameter(description = "리포트 ID", required = true)
            @PathVariable Long reportId) {
        DownloadResponse response = downloadTimer.record(() -> {
            DownloadResponse result = reportService.downloadReport(reportId);
            downloadCounter.increment();
            return result;
        });

        return ResponseEntity.ok(response);
    }


    public ReportController(ReportService reportService, MeterRegistry meterRegistry) {
        this.reportService = reportService;
        this.meterRegistry = meterRegistry;

        // 타이머 메트릭 생성 (Grafana 대시보드에서 사용하는 이름과 일치)
        this.downloadTimer = Timer.builder("http_report_download_seconds")
                .description("리포트 다운로드 처리 시간")
                .tag("outcome", "success")
                .publishPercentiles(0.5, 0.95, 0.99) // Prometheus에 백분위수 히스토그램 게시
                .register(meterRegistry);

        // 카운터 메트릭 생성
        this.downloadCounter = Counter.builder("report_download_total")
                .description("총 리포트 다운로드 요청 수")
                .register(meterRegistry);
    }


}
