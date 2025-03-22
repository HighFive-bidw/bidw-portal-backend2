package com.ktds.bidw.aiquery.repository;

import com.ktds.bidw.aiquery.domain.ChatHistory;
import com.ktds.bidw.aiquery.repository.entity.ChatHistoryEntity;
import com.ktds.bidw.aiquery.repository.jpa.ChatHistoryJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

/**
 * 대화 히스토리 저장소 구현 클래스입니다.
 */
@Repository
@RequiredArgsConstructor
public class ChatHistoryRepositoryImpl implements ChatHistoryRepository {

    private final ChatHistoryJpaRepository chatHistoryJpaRepository;

    /**
     * 대화 히스토리를 저장합니다.
     *
     * @param chatHistory 대화 히스토리
     * @return 저장된 대화 히스토리
     */
    @Override
    public ChatHistory save(ChatHistory chatHistory) {
        ChatHistoryEntity entity = new ChatHistoryEntity(
                chatHistory.getUserId(),
                chatHistory.getReportId(),
                chatHistory.getReportName(),
                chatHistory.getQuestion(),
                chatHistory.getAnswer()
        );

        ChatHistoryEntity savedEntity = chatHistoryJpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    /**
     * 사용자 ID로 대화 히스토리를 페이징 조회합니다.
     *
     * @param userId 사용자 ID
     * @param pageable 페이징 정보
     * @return 대화 히스토리 페이지
     */
    @Override
    public Page<ChatHistory> findByUserId(String userId, Pageable pageable) {
        return chatHistoryJpaRepository.findByUserIdOrderByCreatedDateDesc(userId, pageable)
                .map(ChatHistoryEntity::toDomain);
    }
}