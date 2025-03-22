package com.ktds.bidw.aiquery.repository;

import com.ktds.bidw.aiquery.domain.ChatHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 대화 히스토리 저장소 인터페이스입니다.
 */
public interface ChatHistoryRepository {

    /**
     * 대화 히스토리를 저장합니다.
     *
     * @param chatHistory 대화 히스토리
     * @return 저장된 대화 히스토리
     */
    ChatHistory save(ChatHistory chatHistory);

    /**
     * 사용자 ID로 대화 히스토리를 페이징 조회합니다.
     *
     * @param userId 사용자 ID
     * @param pageable 페이징 정보
     * @return 대화 히스토리 페이지
     */
    Page<ChatHistory> findByUserId(String userId, Pageable pageable);
}