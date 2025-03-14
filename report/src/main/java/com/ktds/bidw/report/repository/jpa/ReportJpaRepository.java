package com.ktds.bidw.report.repository.jpa;

import com.ktds.bidw.report.repository.entity.ReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 리포트 엔티티 JPA 저장소 인터페이스입니다.
 */
public interface ReportJpaRepository extends JpaRepository<ReportEntity, Long> {
    
    /**
     * 특정 기간 내에 업데이트된 리포트를 조회합니다.
     *
     * @param startDate 시작 날짜/시간
     * @param endDate 종료 날짜/시간
     * @return 필터링된 리포트 엔티티 목록
     */
    List<ReportEntity> findByLastUpdatedBetween(LocalDateTime startDate, LocalDateTime endDate);
}
