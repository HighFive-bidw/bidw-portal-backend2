package com.ktds.bidw.aiquery.repository.jpa;

import com.ktds.bidw.aiquery.repository.entity.ChatHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 대화 히스토리 엔티티 JPA 저장소 인터페이스입니다.
 */
public interface ChatHistoryJpaRepository extends JpaRepository<ChatHistoryEntity, Long> {

    /**
     * 사용자 ID로 대화 히스토리를 페이징 조회합니다.
     *
     * @param userId 사용자 ID
     * @param pageable 페이징 정보
     * @return 대화 히스토리 엔티티 페이지
     */
    Page<ChatHistoryEntity> findByUserIdOrderByCreatedDateDesc(String userId, Pageable pageable);
}