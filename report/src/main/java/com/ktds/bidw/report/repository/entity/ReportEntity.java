package com.ktds.bidw.report.repository.entity;

import com.ktds.bidw.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 리포트 엔티티 클래스입니다.
 */
@Entity
@Table(name = "reports")
@Getter
@NoArgsConstructor
public class ReportEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    
    @Column(name = "data_reference")
    private String dataReference;
    
    /**
     * 리포트 엔티티 객체를 생성합니다.
     *
     * @param name 리포트 이름
     * @param description 리포트 설명
     * @param lastUpdated 마지막 업데이트 시간
     * @param dataReference 데이터 참조 정보
     */
    public ReportEntity(String name, String description, LocalDateTime lastUpdated, String dataReference) {
        this.name = name;
        this.description = description;
        this.lastUpdated = lastUpdated;
        this.dataReference = dataReference;
    }
}
