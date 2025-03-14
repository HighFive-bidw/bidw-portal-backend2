package com.ktds.bidw.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 인증 필터 클래스입니다.
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtTokenProvider jwtTokenProvider;
    
    /**
     * HTTP 요청에 대한 JWT 토큰 검증 및 인증 처리를 수행합니다.
     *
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param filterChain 필터 체인
     * @throws ServletException 서블릿 예외 발생 시
     * @throws IOException I/O 예외 발생 시
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String jwt = resolveToken(request);
        
        if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
            Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        filterChain.doFilter(request, response);
    }
    
    /**
     * HTTP 요청에서 JWT 토큰을 추출합니다.
     *
     * @param request HTTP 요청
     * @return JWT 토큰 (토큰이 없는 경우 null)
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
