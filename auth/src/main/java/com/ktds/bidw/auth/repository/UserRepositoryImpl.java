package com.ktds.bidw.auth.repository;

import com.ktds.bidw.auth.domain.User;
import com.ktds.bidw.auth.repository.entity.UserEntity;
import com.ktds.bidw.auth.repository.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 사용자 정보 저장소 구현 클래스입니다.
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    
    /**
     * 사용자 ID로 사용자 정보를 조회합니다.
     *
     * @param username 사용자 ID
     * @return 사용자 정보
     */
    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username)
                .map(UserEntity::toDomain);
    }
}
