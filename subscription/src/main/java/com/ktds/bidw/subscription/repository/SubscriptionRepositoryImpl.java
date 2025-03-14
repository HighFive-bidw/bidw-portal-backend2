package com.ktds.bidw.subscription.repository;

import com.ktds.bidw.subscription.domain.Subscription;
import com.ktds.bidw.subscription.repository.entity.SubscriptionEntity;
import com.ktds.bidw.subscription.repository.jpa.SubscriptionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 구독 저장소 구현 클래스입니다.
 */
@Repository
@RequiredArgsConstructor
public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    private final SubscriptionJpaRepository subscriptionJpaRepository;
    
    /**
     * 사용자 ID로 구독 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 구독 목록
     */
    @Override
    public List<Subscription> findByUserId(String userId) {
        return subscriptionJpaRepository.findByUserId(userId).stream()
                .map(SubscriptionEntity::toDomain)
                .collect(Collectors.toList());
    }
    
    /**
     * 구독 ID와 사용자 ID로 구독 정보를 조회합니다.
     *
     * @param id 구독 ID
     * @param userId 사용자 ID
     * @return 구독 정보
     */
    @Override
    public Optional<Subscription> findByIdAndUserId(Long id, String userId) {
        return subscriptionJpaRepository.findByIdAndUserId(id, userId)
                .map(SubscriptionEntity::toDomain);
    }
    
    /**
     * 구독 정보를 저장합니다.
     *
     * @param subscription 구독 정보
     * @return 저장된 구독 정보
     */
    @Override
    public Subscription save(Subscription subscription) {
        SubscriptionEntity entity = new SubscriptionEntity(
                subscription.getId(),
                subscription.getUserId(),
                subscription.getReportId(),
                subscription.getSubscribedDate(),
                subscription.getReportName(),
                subscription.getLastUpdated()
        );
        
        SubscriptionEntity savedEntity = subscriptionJpaRepository.save(entity);
        return savedEntity.toDomain();
    }
    
    /**
     * 구독 정보를 삭제합니다.
     *
     * @param subscription 구독 정보
     */
    @Override
    public void delete(Subscription subscription) {
        subscriptionJpaRepository.findById(subscription.getId())
                .ifPresent(subscriptionJpaRepository::delete);
    }
    
    /**
     * 사용자 ID로 구독 수를 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 구독 수
     */
    @Override
    public int countByUserId(String userId) {
        return subscriptionJpaRepository.countByUserId(userId);
    }
}
