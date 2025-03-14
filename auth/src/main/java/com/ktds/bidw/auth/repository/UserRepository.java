package com.ktds.bidw.auth.repository;

import com.ktds.bidw.auth.domain.User;

import java.util.Optional;

/**
 * 사용자 정보 저장소 인터페이스입니다.
 */
public interface UserRepository {
    
    /**
     * 사용자 ID로 사용자 정보를 조회합니다.
     *
     * @param username 사용자 ID
     * @return 사용자 정보
     */
    Optional<User> findByUsername(String username);
}
