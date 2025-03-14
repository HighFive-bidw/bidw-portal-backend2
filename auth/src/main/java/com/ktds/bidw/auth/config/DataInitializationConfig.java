package com.ktds.bidw.auth.config;

import com.ktds.bidw.auth.domain.Role;
import com.ktds.bidw.auth.repository.entity.UserEntity;
import com.ktds.bidw.auth.repository.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 샘플 사용자 데이터를 생성하는 설정 클래스입니다.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataInitializationConfig {

    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 애플리케이션 시작 시 샘플 사용자 데이터를 생성합니다.
     *
     * @return CommandLineRunner 인스턴스
     */
    @Bean
    public CommandLineRunner initializeData() {
        return args -> {
            log.info("Initializing sample user data...");

            // 일반 사용자 생성 (user01 ~ user05)
            for (int i = 1; i <= 5; i++) {
                String username = String.format("user%02d", i);
                createUserIfNotExists(username, "P@ssw0rd$", Role.USER);
            }

            // 관리자 생성 (admin01)
            createUserIfNotExists("admin01", "P@ssw0rd$", Role.ADMIN);

            log.info("Sample user data initialization completed.");
        };
    }

    /**
     * 사용자가 존재하지 않는 경우 새로 생성합니다.
     *
     * @param username 사용자 ID
     * @param password 비밀번호
     * @param role 역할
     */
    private void createUserIfNotExists(String username, String password, Role role) {
        if (!userJpaRepository.findByUsername(username).isPresent()) {
            UserEntity userEntity = new UserEntity(username, passwordEncoder.encode(password));
            userEntity.addRole(role);

            if (role == Role.ADMIN) {
                // 관리자는 USER 역할도 가지도록 함
                userEntity.addRole(Role.USER);
            }

            userJpaRepository.save(userEntity);
            log.info("Created user: {}, role: {}", username, role);
        } else {
            log.info("User already exists: {}", username);
        }
    }
}
