package com.ktds.bidw.aiquery.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 대화 히스토리 도메인 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatHistory {

    private Long id;
    private String userId;
    private Long reportId;
    private String reportName;
    private String question;
    private String answer;
    private LocalDateTime createdAt;
}