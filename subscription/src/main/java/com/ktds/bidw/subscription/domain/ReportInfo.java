package com.ktds.bidw.subscription.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 리포트 정보 도메인 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReportInfo {
    
    private Long id;
    private String name;
    private LocalDateTime lastUpdated;
}
