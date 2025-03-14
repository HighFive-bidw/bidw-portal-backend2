package com.ktds.bidw.report.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 다운로드 토큰 도메인 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DownloadToken {
    
    private Long id;
    private String token;
    private Long reportId;
    private String fileName;
    private LocalDateTime expiryTime;
    private boolean used;
    
    /**
     * 토큰의 만료 여부를 확인합니다.
     *
     * @return 만료 여부
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryTime);
    }
    
    /**
     * 토큰을 사용 처리합니다.
     */
    public void markAsUsed() {
        this.used = true;
    }
}
