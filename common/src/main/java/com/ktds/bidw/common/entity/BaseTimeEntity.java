package com.ktds.bidw.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * 생성 및 수정 시간을 자동으로 관리하는 기본 엔티티 클래스입니다.
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {
    
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;
    
    @LastModifiedDate
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
