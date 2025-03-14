package com.ktds.bidw.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 사용자 인증 정보를 로드하는 서비스 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    
    /**
     * 사용자 이름으로 사용자 인증 정보를 로드합니다.
     *
     * @param username 사용자 이름
     * @return 사용자 인증 정보
     * @throws UsernameNotFoundException 사용자를 찾을 수 없는 경우
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 실제 구현은 각 서비스별 인증 방식에 따라 다르게 구현됨
        throw new UnsupportedOperationException("This method should be implemented by specific authentication service");
    }
}