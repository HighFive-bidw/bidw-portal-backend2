package com.ktds.bidw.subscription.repository;

import com.ktds.bidw.subscription.domain.Subscription;

import java.util.List;
import java.util.Optional;

/**
 * 구독 저장소 인터페이스입니다.
 */
public interface SubscriptionRepository {
    
    /**
     * 사용자 ID로 구독 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 구독 목록
     */
    List<Subscription> findByUserId(String userId);
    
    /**
     * 구독 ID와 사용자 ID로 구독 정보를 조회합니다.
     *
     * @param id 구독 ID
     * @param userId 사용자 ID
     * @return 구독 정보
     */
    Optional<Subscription> findByIdAndUserId(Long id, String userId);
    
    /**
     * 구독 정보를 저장합니다.
     *
     * @param subscription 구독 정보
     * @return 저장된 구독 정보
     */
    Subscription save(Subscription subscription);
    
    /**
     * 구독 정보를 삭제합니다.
     *
     * @param subscription 구독 정보
     */
    void delete(Subscription subscription);
    
    /**
     * 사용자 ID로 구독 수를 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 구독 수
     */
    int countByUserId(String userId);
}
