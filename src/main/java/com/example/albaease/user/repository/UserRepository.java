package com.example.albaease.user.repository;
import com.example.albaease.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLoginId(String loginId);  // 로그인 아이디로 사용자 조회
    boolean existsByLoginId(String id);  //  (로그인 ID 중복 검사)
}
