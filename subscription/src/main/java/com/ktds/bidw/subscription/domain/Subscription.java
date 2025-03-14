package com.ktds.bidw.subscription.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 구독 도메인 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {
    
    private Long id;
    private String userId;
    private Long reportId;
    private LocalDateTime subscribedDate;
    private String reportName;
    private LocalDateTime lastUpdated;
}
