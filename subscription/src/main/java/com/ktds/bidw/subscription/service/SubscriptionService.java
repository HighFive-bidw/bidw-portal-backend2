package com.ktds.bidw.subscription.service;

import com.ktds.bidw.subscription.dto.SubscriptionListDTO;
import com.ktds.bidw.subscription.dto.SubscriptionResponse;

import java.util.List;

/**
 * 구독 관련 서비스 인터페이스입니다.
 */
public interface SubscriptionService {
    
    /**
     * 사용자의 구독 리포트 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 구독 리포트 목록
     */
    List<SubscriptionListDTO> getSubscriptionList(String userId);
    
    /**
     * 리포트를 구독합니다.
     *
     * @param userId 사용자 ID
     * @param reportId 리포트 ID
     * @return 구독 처리 결과
     */
    SubscriptionResponse subscribeReport(String userId, Long reportId);
    
    /**
     * 리포트 구독을 취소합니다.
     *
     * @param subscriptionId 구독 ID
     * @param userId 사용자 ID
     * @return 구독 취소 처리 결과
     */
    SubscriptionResponse unsubscribeReport(Long subscriptionId, String userId);
}
