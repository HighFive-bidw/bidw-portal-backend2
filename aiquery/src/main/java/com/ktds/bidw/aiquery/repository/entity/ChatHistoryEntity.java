package com.ktds.bidw.aiquery.repository.entity;

import com.ktds.bidw.aiquery.domain.ChatHistory;
import com.ktds.bidw.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 대화 히스토리 엔티티 클래스입니다.
 */
@Entity
@Table(name = "chat_history")
@Getter
@NoArgsConstructor
public class ChatHistoryEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "report_id", nullable = false)
    private Long reportId;

    @Column(name = "report_name")
    private String reportName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String question;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String answer;

    /**
     * 대화 히스토리 엔티티 객체를 생성합니다.
     *
     * @param userId 사용자 ID
     * @param reportId 리포트 ID
     * @param reportName 리포트 이름
     * @param question 질문 내용
     * @param answer 응답 내용
     */
    public ChatHistoryEntity(String userId, Long reportId, String reportName, String question, String answer) {
        this.userId = userId;
        this.reportId = reportId;
        this.reportName = reportName;
        this.question = question;
        this.answer = answer;
    }

    /**
     * 엔티티를 도메인 객체로 변환합니다.
     *
     * @return 도메인 객체
     */
    public ChatHistory toDomain() {
        return new ChatHistory(
                this.id,
                this.userId,
                this.reportId,
                this.reportName,
                this.question,
                this.answer,
                this.getCreatedDate()
        );
    }
}