package com.example.albaease.auth.jwt;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class JwtFilterTest {
    @Autowired
    private JwtUtil jwtUtil;  // 실제 JwtUtil을 Autowired로 주입받기

    @Test
    void testExtractUserIdFromToken() {
        // 임시로 사용할 JWT 토큰 (임의의 userId 값 포함)
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwicm9sZSI6Ik9XTkVSIiwiaWF0IjoxNzM5NDY0MjkxLCJleHAiOjE3Mzk0Njc4OTF9.BBxcaJli2UFuGNqqcTfVw8j--_x-vXYsdA5xkgy38NE";

        // 실제 JwtUtil을 사용해서 userId 추출
        String userId = jwtUtil.extractUserId(token);

        System.out.println("테스트코드입니다 " + userId);
    }




}