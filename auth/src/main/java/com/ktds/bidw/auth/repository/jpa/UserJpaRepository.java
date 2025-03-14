package com.ktds.bidw.auth.repository.jpa;

import com.ktds.bidw.auth.repository.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 사용자 엔티티 JPA 저장소 인터페이스입니다.
 */
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    
    /**
     * 사용자 ID로 사용자 엔티티를 조회합니다.
     *
     * @param username 사용자 ID
     * @return 사용자 엔티티
     */
    Optional<UserEntity> findByUsername(String username);
}
