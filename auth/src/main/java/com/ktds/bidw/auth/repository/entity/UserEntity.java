package com.ktds.bidw.auth.repository.entity;

import com.ktds.bidw.auth.domain.Role;
import com.ktds.bidw.auth.domain.User;
import com.ktds.bidw.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * 사용자 정보를 저장하는 엔티티 클래스입니다.
 */
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @Column(nullable = false)
    private String password;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();
    
    /**
     * 사용자 엔티티 객체를 생성합니다.
     *
     * @param username 사용자 ID
     * @param password 암호화된 비밀번호
     */
    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    /**
     * 사용자 역할을 추가합니다.
     *
     * @param role 추가할 역할
     */
    public void addRole(Role role) {
        this.roles.add(role);
    }
    
    /**
     * 엔티티를 도메인 객체로 변환합니다.
     *
     * @return 도메인 객체
     */
    public User toDomain() {
        User user = new User(this.username, this.password);
        this.roles.forEach(user::addRole);
        return user;
    }
}
