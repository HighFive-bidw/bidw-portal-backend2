package com.ktds.bidw.common.security;

import com.ktds.bidw.common.dto.JwtTokenDTO;
import io.jsonwebtoken.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * JWT 토큰 생성 및 검증을 담당하는 클래스입니다.
 */
@Component
public class JwtTokenProvider {

    @Getter
    @Value("${jwt.access-token-validity}")
    private long tokenValidityInMilliseconds;
    
    private final SecretKey key;
    private final JwtParser jwtParser;
    private final Set<String> blacklistedTokens = Collections.newSetFromMap(new ConcurrentHashMap<>());
    
    /**
     * 생성자를 통해 JWT 시크릿 키를 초기화합니다.
     *
     * @param secretKey JWT 시크릿 키
     */
    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey) {
        this.key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        this.jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }
    
    /**
     * 사용자 인증 정보를 기반으로 JWT 토큰을 생성합니다.
     *
     * @param authentication 인증 정보
     * @return JWT 토큰 DTO
     */
    public JwtTokenDTO createToken(Authentication authentication) {
        String username = authentication.getName();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInMilliseconds);

        String token = Jwts.builder()
                .setSubject(username)
                .claim("auth", authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(",")))
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();

        // 수정된 부분: Date를 LocalDateTime으로 변환하는 로직
        LocalDateTime expiryDateTime = validity.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return new JwtTokenDTO(token, expiryDateTime);
    }
    
    /**
     * JWT 토큰에서 인증 정보를 추출합니다.
     *
     * @param token JWT 토큰
     * @return 인증 정보
     */
    public Authentication getAuthentication(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                .filter(auth -> !auth.trim().isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
    
    /**
     * JWT 토큰의 유효성을 검사합니다.
     *
     * @param token JWT 토큰
     * @return 토큰 유효 여부
     */
    public boolean validateToken(String token) {
        if (blacklistedTokens.contains(token)) {
            return false;
        }
        
        try {
            jwtParser.parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    
    /**
     * 토큰을 무효화합니다 (로그아웃).
     *
     * @param token 무효화할 토큰
     */
    public void invalidateToken(String token) {
        blacklistedTokens.add(token);
    }
}
