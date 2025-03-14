package com.ktds.bidw.auth.service;

import com.ktds.bidw.auth.dto.LoginRequest;
import com.ktds.bidw.auth.dto.LoginResponse;
import com.ktds.bidw.auth.dto.LogoutRequest;
import com.ktds.bidw.auth.dto.LogoutResponse;

/**
 * 인증 관련 서비스 인터페이스입니다.
 */
public interface AuthService {
    
    /**
     * 사용자 로그인을 처리합니다.
     *
     * @param request 로그인 요청 정보
     * @return 로그인 응답 정보 (토큰 포함)
     */
    LoginResponse login(LoginRequest request);
    
    /**
     * 사용자 로그아웃을 처리합니다.
     *
     * @param request 로그아웃 요청 정보
     * @return 로그아웃 응답 정보
     */
    LogoutResponse logout(LogoutRequest request);
}
