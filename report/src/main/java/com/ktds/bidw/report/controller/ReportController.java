package com.ktds.bidw.report.controller;

import com.ktds.bidw.report.dto.DownloadResponse;
import com.ktds.bidw.report.dto.ReportDetailDTO;
import com.ktds.bidw.report.dto.ReportFilterRequest;
import com.ktds.bidw.report.dto.ReportListDTO;
import com.ktds.bidw.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 리포트 관련 API 컨트롤러입니다.
 */
@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Tag(name = "리포트 API", description = "리포트 조회 및 다운로드 API")
public class ReportController {

    private final ReportService reportService;
    
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
     * @param request 필터링 조건
     * @return 필터링된 리포트 목록
     */
    @GetMapping("/filter")
    @Operation(summary = "리포트 필터링", description = "날짜 기준으로 필터링된 리포트 목록을 조회합니다.")
    public ResponseEntity<List<ReportListDTO>> getFilteredReports(@RequestBody ReportFilterRequest request) {
        List<ReportListDTO> reports = reportService.getFilteredReports(request.getStartDate(), request.getEndDate());
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
        DownloadResponse downloadResponse = reportService.downloadReport(reportId);
        return ResponseEntity.ok(downloadResponse);
    }
}
