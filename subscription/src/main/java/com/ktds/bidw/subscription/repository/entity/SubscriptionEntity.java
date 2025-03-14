package com.ktds.bidw.subscription.repository.entity;

import com.ktds.bidw.common.entity.BaseTimeEntity;
import com.ktds.bidw.subscription.domain.Subscription;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 구독 엔티티 클래스입니다.
 */
@Entity
@Table(name = "subscriptions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id", nullable = false)
    private String userId;
    
    @Column(name = "report_id", nullable = false)
    private Long reportId;
    
    @Column(name = "subscribed_date", nullable = false)
    private LocalDateTime subscribedDate;
    
    @Column(name = "report_name", nullable = false)
    private String reportName;
    
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    
    /**
     * 엔티티를 도메인 객체로 변환합니다.
     *
     * @return 도메인 객체
     */
    public Subscription toDomain() {
        return new Subscription(
                this.id,
                this.userId,
                this.reportId,
                this.subscribedDate,
                this.reportName,
                this.lastUpdated
        );
    }
}
