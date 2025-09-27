package com.labify.backend.config;

//JPA 관련 전역 설정 모아두는 클래스!
//user 테이블의 createdAt, updatedAt 자동 관리하게 사용
//추후 lastLoginAt도 업데이트 할 예정

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
}
