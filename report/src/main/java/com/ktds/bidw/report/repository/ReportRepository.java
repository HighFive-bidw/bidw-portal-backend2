package com.ktds.bidw.report.repository;

import com.ktds.bidw.report.domain.Report;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 리포트 저장소 인터페이스입니다.
 */
public interface ReportRepository {
    
    /**
     * 모든 리포트를 조회합니다.
     *
     * @return 리포트 목록
     */
    List<Report> findAll();
    
    /**
     * 특정 기간 내에 업데이트된 리포트를 조회합니다.
     *
     * @param startDate 시작 날짜/시간
     * @param endDate 종료 날짜/시간
     * @return 필터링된 리포트 목록
     */
    List<Report> findByLastUpdatedBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * ID로 리포트를 조회합니다.
     *
     * @param reportId 리포트 ID
     * @return 리포트 (존재하지 않을 경우 빈 Optional)
     */
    Optional<Report> findById(Long reportId);
}
