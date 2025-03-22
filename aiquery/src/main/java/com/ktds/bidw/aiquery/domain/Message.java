package com.ktds.bidw.aiquery.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 대화 메시지 도메인 클래스입니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    public enum Role {
        USER, AI
    }

    private Role role;
    private String content;
    private LocalDateTime timestamp;

    /**
     * 메시지 객체를 생성합니다.
     *
     * @param role 역할 (사용자/AI)
     * @param content 메시지 내용
     */
    public Message(Role role, String content) {
        this.role = role;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }
}