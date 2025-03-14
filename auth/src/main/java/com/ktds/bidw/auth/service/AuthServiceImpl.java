package com.ktds.bidw.auth.service;

import com.ktds.bidw.auth.domain.User;
import com.ktds.bidw.auth.dto.LoginRequest;
import com.ktds.bidw.auth.dto.LoginResponse;
import com.ktds.bidw.auth.dto.LogoutRequest;
import com.ktds.bidw.auth.dto.LogoutResponse;
import com.ktds.bidw.auth.exception.InvalidCredentialsException;
import com.ktds.bidw.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * 인증 관련 서비스 구현 클래스입니다.
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    
    /**
     * 사용자 로그인을 처리합니다.
     *
     * @param request 로그인 요청 정보
     * @return 로그인 응답 정보 (토큰 포함)
     * @throws InvalidCredentialsException 사용자 인증 실패 시
     */
    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("사용자 ID 또는 비밀번호가 잘못되었습니다."));
        
        validateCredentials(user, request.getPassword());
        
        String token = tokenProvider.createToken(user);
        String userRole = user.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));
        
        LocalDateTime expiryTime = LocalDateTime.now().plusSeconds(
                tokenProvider.getTokenValidityInMilliseconds() / 1000);
        
        return new LoginResponse(token, userRole, expiryTime);
    }
    
    /**
     * 사용자 로그아웃을 처리합니다.
     *
     * @param request 로그아웃 요청 정보
     * @return 로그아웃 응답 정보
     */
    @Override
    @Transactional
    public LogoutResponse logout(LogoutRequest request) {
        tokenProvider.invalidateToken(request.getToken());
        return new LogoutResponse("로그아웃 되었습니다.");
    }
    
    /**
     * 사용자 자격 증명을 검증합니다.
     *
     * @param user 사용자 정보
     * @param password 입력된 비밀번호
     * @throws InvalidCredentialsException 비밀번호가 일치하지 않을 때
     */
    private void validateCredentials(User user, String password) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("사용자 ID 또는 비밀번호가 잘못되었습니다.");
        }
    }
}
