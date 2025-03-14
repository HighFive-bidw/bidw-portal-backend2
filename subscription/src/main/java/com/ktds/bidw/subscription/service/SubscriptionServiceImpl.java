package com.ktds.bidw.subscription.service;

import com.ktds.bidw.subscription.domain.ReportInfo;
import com.ktds.bidw.subscription.domain.Subscription;
import com.ktds.bidw.subscription.dto.SubscriptionListDTO;
import com.ktds.bidw.subscription.dto.SubscriptionResponse;
import com.ktds.bidw.subscription.exception.SubscriptionLimitExceededException;
import com.ktds.bidw.subscription.exception.SubscriptionNotFoundException;
import com.ktds.bidw.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 구독 관련 서비스 구현 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final ReportClient reportClient;
    
    @Value("${subscription.max-limit}")
    private int maxSubscriptionLimit;
    
    /**
     * 사용자의 구독 리포트 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 구독 리포트 목록
     */
    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionListDTO> getSubscriptionList(String userId) {
        return subscriptionRepository.findByUserId(userId).stream()
                .map(this::convertToSubscriptionListDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 리포트를 구독합니다.
     *
     * @param userId 사용자 ID
     * @param reportId 리포트 ID
     * @return 구독 처리 결과
     */
    @Override
    @Transactional
    public SubscriptionResponse subscribeReport(String userId, Long reportId) {
        // 최대 구독 수 제한 확인
        checkSubscriptionLimit(userId);
        
        // 리포트 정보 조회
        ReportInfo reportInfo = reportClient.getReportInfo(reportId);
        
        // 구독 정보 생성 및 저장
        Subscription subscription = new Subscription(
                null,
                userId,
                reportId,
                LocalDateTime.now(),
                reportInfo.getName(),
                reportInfo.getLastUpdated()
        );
        
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        
        return new SubscriptionResponse("리포트가 성공적으로 구독되었습니다.", savedSubscription.getId());
    }
    
    /**
     * 리포트 구독을 취소합니다.
     *
     * @param subscriptionId 구독 ID
     * @param userId 사용자 ID
     * @return 구독 취소 처리 결과
     */
    @Override
    @Transactional
    public SubscriptionResponse unsubscribeReport(Long subscriptionId, String userId) {
        // 구독 정보 조회
        Subscription subscription = subscriptionRepository.findByIdAndUserId(subscriptionId, userId)
                .orElseThrow(() -> new SubscriptionNotFoundException("구독 정보를 찾을 수 없습니다: " + subscriptionId));
        
        // 구독 취소 처리
        subscriptionRepository.delete(subscription);
        
        return new SubscriptionResponse("리포트 구독이 취소되었습니다.", subscriptionId);
    }
    
    /**
     * 사용자의 구독 수가 최대 한도를 초과하는지 확인합니다.
     *
     * @param userId 사용자 ID
     * @throws SubscriptionLimitExceededException 구독 수 초과 시
     */
    private void checkSubscriptionLimit(String userId) {
        int currentSubscriptionCount = subscriptionRepository.countByUserId(userId);
        
        if (currentSubscriptionCount >= maxSubscriptionLimit) {
            throw new SubscriptionLimitExceededException(
                    "최대 구독 한도에 도달했습니다. 다른 구독을 취소한 후 다시 시도해주세요.");
        }
    }
    
    /**
     * Subscription을 SubscriptionListDTO로 변환합니다.
     *
     * @param subscription Subscription 객체
     * @return SubscriptionListDTO 객체
     */
    private SubscriptionListDTO convertToSubscriptionListDTO(Subscription subscription) {
        return new SubscriptionListDTO(
                subscription.getId(),
                subscription.getReportId(),
                subscription.getReportName(),
                subscription.getSubscribedDate(),
                subscription.getLastUpdated()
        );
    }
}
