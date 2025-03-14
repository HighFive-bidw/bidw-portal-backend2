package com.ktds.bidw.report.repository;

import com.ktds.bidw.report.domain.Report;
import com.ktds.bidw.report.domain.ReportData;
import com.ktds.bidw.report.repository.entity.ReportDataEntity;
import com.ktds.bidw.report.repository.entity.ReportEntity;
import com.ktds.bidw.report.repository.jpa.ReportDataJpaRepository;
import com.ktds.bidw.report.repository.jpa.ReportJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 리포트 저장소 구현 클래스입니다.
 */
@Repository
@RequiredArgsConstructor
public class ReportRepositoryImpl implements ReportRepository {

    private final ReportJpaRepository reportJpaRepository;
    private final ReportDataJpaRepository reportDataJpaRepository;
    
    /**
     * 모든 리포트를 조회합니다.
     *
     * @return 리포트 목록
     */
    @Override
    public List<Report> findAll() {
        return reportJpaRepository.findAll().stream()
                .map(this::convertToReportDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * 특정 기간 내에 업데이트된 리포트를 조회합니다.
     *
     * @param startDate 시작 날짜/시간
     * @param endDate 종료 날짜/시간
     * @return 필터링된 리포트 목록
     */
    @Override
    public List<Report> findByLastUpdatedBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return reportJpaRepository.findByLastUpdatedBetween(startDate, endDate).stream()
                .map(this::convertToReportDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * ID로 리포트를 조회합니다.
     *
     * @param reportId 리포트 ID
     * @return 리포트 (존재하지 않을 경우 빈 Optional)
     */
    @Override
    public Optional<Report> findById(Long reportId) {
        return reportJpaRepository.findById(reportId)
                .map(this::convertToReportDomain);
    }
    
    /**
     * ReportEntity를 Report 도메인 객체로 변환합니다.
     *
     * @param reportEntity 리포트 엔티티
     * @return 리포트 도메인 객체
     */
    private Report convertToReportDomain(ReportEntity reportEntity) {
        Optional<ReportDataEntity> reportDataEntityOpt = reportDataJpaRepository.findByReportId(reportEntity.getId());
        
        ReportData reportData = reportDataEntityOpt
                .map(ReportDataEntity::toDomain)
                .orElse(new ReportData(List.of(), List.of()));
        
        return new Report(
                reportEntity.getId(),
                reportEntity.getName(),
                reportEntity.getDescription(),
                reportEntity.getLastUpdated(),
                reportData
        );
    }
}
