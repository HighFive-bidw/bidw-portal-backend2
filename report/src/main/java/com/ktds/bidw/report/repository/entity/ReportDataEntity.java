package com.ktds.bidw.report.repository.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ktds.bidw.common.entity.BaseTimeEntity;
import com.ktds.bidw.report.domain.ReportData;
import com.ktds.bidw.report.exception.FileStorageException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 리포트 데이터 엔티티 클래스입니다.
 */
@Entity
@Table(name = "report_data")
@Getter
@NoArgsConstructor
public class ReportDataEntity extends BaseTimeEntity {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "report_id")
    private Long reportId;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String headers;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String data;
    
    /**
     * 리포트 데이터 엔티티 객체를 생성합니다.
     *
     * @param reportId 리포트 ID
     * @param headers 헤더 목록 (JSON 문자열)
     * @param data 데이터 행 목록 (JSON 문자열)
     */
    public ReportDataEntity(Long reportId, String headers, String data) {
        this.reportId = reportId;
        this.headers = headers;
        this.data = data;
    }
    
    /**
     * 엔티티를 도메인 객체로 변환합니다.
     *
     * @return 도메인 객체
     */
    public ReportData toDomain() {
        try {
            List<String> headersList = objectMapper.readValue(headers, new TypeReference<List<String>>() {});
            List<Map<String, Object>> dataList = objectMapper.readValue(data, new TypeReference<List<Map<String, Object>>>() {});
            
            return new ReportData(headersList, dataList);
        } catch (JsonProcessingException e) {
            throw new FileStorageException("리포트 데이터 변환 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
