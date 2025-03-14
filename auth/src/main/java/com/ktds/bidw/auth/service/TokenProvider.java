package com.ktds.bidw.auth.service;

import com.ktds.bidw.auth.domain.User;
import io.jsonwebtoken.*;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JWT 토큰 생성 및 검증을 담당하는 클래스입니다.
 */
@Component
public class TokenProvider {

    @Getter
    @Value("${jwt.access-token-validity}")
    private long tokenValidityInMilliseconds;
    
    private final SecretKey key;
    private final JwtParser jwtParser;
    private final Set<String> blacklistedTokens = Collections.newSetFromMap(new ConcurrentHashMap<>());
    
    public TokenProvider(@Value("${jwt.secret-key}") String secretKey) {
        this.key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
        this.jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
    }
    
    /**
     * 사용자 정보를 기반으로 JWT 토큰을 생성합니다.
     *
     * @param user 사용자 정보
     * @return 생성된 JWT 토큰
     */
    public String createToken(User user) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInMilliseconds);
        
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", user.getRoles())
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(key)
                .compact();
    }
    
    /**
     * JWT 토큰의 유효성을 검사합니다.
     *
     * @param token 검증할 JWT 토큰
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
     * JWT 토큰으로부터 사용자 ID를 추출합니다.
     *
     * @param token JWT 토큰
     * @return 사용자 ID
     */
    public String getUsernameFromToken(String token) {
        return jwtParser.parseClaimsJws(token).getBody().getSubject();
    }
    
    /**
     * 토큰을 무효화합니다(로그아웃).
     *
     * @param token 무효화할 토큰
     */
    public void invalidateToken(String token) {
        blacklistedTokens.add(token);
    }
}
