package com.ktds.bidw.report.repository.jpa;

import com.ktds.bidw.report.repository.entity.ReportDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 리포트 데이터 엔티티 JPA 저장소 인터페이스입니다.
 */
public interface ReportDataJpaRepository extends JpaRepository<ReportDataEntity, Long> {
    
    /**
     * 리포트 ID로 리포트 데이터를 조회합니다.
     *
     * @param reportId 리포트 ID
     * @return 리포트 데이터 엔티티
     */
    Optional<ReportDataEntity> findByReportId(Long reportId);
}
