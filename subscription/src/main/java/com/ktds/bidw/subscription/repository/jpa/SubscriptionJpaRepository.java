package com.ktds.bidw.subscription.repository.jpa;

import com.ktds.bidw.subscription.repository.entity.SubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 구독 엔티티 JPA 저장소 인터페이스입니다.
 */
public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionEntity, Long> {
    
    /**
     * 사용자 ID로 구독 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 구독 엔티티 목록
     */
    List<SubscriptionEntity> findByUserId(String userId);
    
    /**
     * 구독 ID와 사용자 ID로 구독 정보를 조회합니다.
     *
     * @param id 구독 ID
     * @param userId 사용자 ID
     * @return 구독 엔티티
     */
    Optional<SubscriptionEntity> findByIdAndUserId(Long id, String userId);
    
    /**
     * 사용자 ID로 구독 수를 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 구독 수
     */
    int countByUserId(String userId);
}
