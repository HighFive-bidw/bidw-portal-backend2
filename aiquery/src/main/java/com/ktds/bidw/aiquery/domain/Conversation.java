package com.ktds.bidw.aiquery.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 대화 컨텍스트 도메인 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {

    private String id;
    private String userId;
    private Long reportId;
    private List<Message> messages = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 대화 객체를 생성합니다.
     *
     * @param id 대화 ID
     * @param userId 사용자 ID
     * @param reportId 리포트 ID
     */
    public Conversation(String id, String userId, Long reportId) {
        this.id = id;
        this.userId = userId;
        this.reportId = reportId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 메시지를 추가합니다.
     *
     * @param message 추가할 메시지
     */
    public void addMessage(Message message) {
        this.messages.add(message);
        this.updatedAt = LocalDateTime.now();
    }
}