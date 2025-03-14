package com.ktds.bidw.common.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 보안 관련 유틸리티 클래스입니다.
 */
public class SecurityUtils {
    
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    /**
     * 비밀번호를 해시화합니다.
     *
     * @param password 원본 비밀번호
     * @return 해시화된 비밀번호
     */
    public static String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
    
    /**
     * 원본 비밀번호와 해시화된 비밀번호가 일치하는지 확인합니다.
     *
     * @param rawPassword 원본 비밀번호
     * @param encodedPassword 해시화된 비밀번호
     * @return 일치 여부
     */
    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
