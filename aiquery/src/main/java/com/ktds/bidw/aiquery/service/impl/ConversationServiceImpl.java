package com.ktds.bidw.aiquery.service.impl;

import com.ktds.bidw.aiquery.domain.Conversation;
import com.ktds.bidw.aiquery.service.ConversationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 인메모리 기반 간소화된 대화 관리 서비스입니다.
 * Redis 연결 문제 해결을 위해 임시로 사용합니다.
 */
@Slf4j
@Service
public class ConversationServiceImpl implements ConversationService {

    // 인메모리 대화 저장소
    private final Map<String, Conversation> conversations = new ConcurrentHashMap<>();

    /**
     * 새로운 대화 컨텍스트를 생성합니다.
     */
    @Override
    public Conversation createConversation(String userId, Long reportId) {
        String conversationId = "conv-" + UUID.randomUUID().toString();
        Conversation conversation = new Conversation(conversationId, userId, reportId);

        conversations.put(conversationId, conversation);
        log.info("Created new in-memory conversation: {}", conversationId);

        return conversation;
    }

    /**
     * 대화 컨텍스트를 조회합니다.
     */
    @Override
    public Conversation getConversation(String conversationId) {
        Conversation conversation = conversations.get(conversationId);

        if (conversation == null) {
            log.warn("Conversation not found: {}, creating new one", conversationId);
            return new Conversation(conversationId, "anonymous", 0L);
        }

        return conversation;
    }

    /**
     * 대화 컨텍스트를 업데이트합니다.
     */
    @Override
    public Conversation updateConversation(Conversation conversation) {
        if (conversation != null && conversation.getId() != null) {
            conversations.put(conversation.getId(), conversation);
            log.debug("Updated conversation: {}", conversation.getId());
        }
        return conversation;
    }
}