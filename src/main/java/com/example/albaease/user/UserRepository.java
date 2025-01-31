package com.example.albaease.user;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
//    User findByUsername(String username);  // 로그인용 메서드
}