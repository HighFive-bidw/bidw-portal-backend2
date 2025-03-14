package com.ktds.bidw.auth.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 사용자 정보를 관리하는 도메인 클래스입니다.
 */
@Getter
@NoArgsConstructor
public class User {

    private Long id;
    private String username;
    private String password;
    private Set<Role> roles = new HashSet<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    /**
     * 사용자 객체를 생성합니다.
     *
     * @param username 사용자 ID
     * @param password 암호화된 비밀번호
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 사용자 역할을 추가합니다.
     *
     * @param role 추가할 역할
     */
    public void addRole(Role role) {
        this.roles.add(role);
        this.updatedAt = LocalDateTime.now();
    }
}
