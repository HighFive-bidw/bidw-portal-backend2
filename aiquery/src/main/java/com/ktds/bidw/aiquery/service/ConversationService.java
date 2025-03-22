package com.ktds.bidw.aiquery.service;

import com.ktds.bidw.aiquery.domain.Conversation;

/**
 * 대화 컨텍스트 관리 서비스 인터페이스입니다.
 */
public interface ConversationService {

    /**
     * 새로운 대화 컨텍스트를 생성합니다.
     *
     * @param userId 사용자 ID
     * @param reportId 리포트 ID
     * @return 생성된 대화 컨텍스트
     */
    Conversation createConversation(String userId, Long reportId);

    /**
     * 대화 컨텍스트를 조회합니다.
     *
     * @param conversationId 대화 컨텍스트 ID
     * @return 대화 컨텍스트
     */
    Conversation getConversation(String conversationId);

    /**
     * 대화 컨텍스트를 업데이트합니다.
     *
     * @param conversation 업데이트할 대화 컨텍스트
     * @return 업데이트된 대화 컨텍스트
     */
    Conversation updateConversation(Conversation conversation);
}