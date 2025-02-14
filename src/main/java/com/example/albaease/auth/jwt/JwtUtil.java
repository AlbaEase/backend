package com.example.albaease.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    private SecretKey secretKey;
    private final long validityInMilliseconds = 3600000;  // JWT 유효 기간 (1시간)

    public JwtUtil(){
        this.secretKey=Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    // JWT 토큰 생성
    public String generateToken(String userId, String role) {
        return Jwts.builder()
                .setSubject(userId)  // 토큰에 저장할 사용자 정보
                .claim("role", role)  // 사용자 역할
                .setIssuedAt(new Date())  // 토큰 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds))  // 만료 시간 (1시간)
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 서명
                .compact();
    }

    // JWT 토큰에서 사용자 ID 추출
    public String extractUserId(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey) // 서명에 사용된 비밀 키 저장
                .parseClaimsJws(token) // 토큰을 파싱해서 jws 클레임 추출
                .getBody() // 클레이 바디 부분 가져옴
                .getSubject(); //userId저장된 subject 값 반환
    }

    // JWT 토큰 만료여부 확인
    public boolean isTokenExpired(String token) {
        //만료시간, 현재시간 비교해서 만료되었는지 확인
        return extractExpirationDate(token).before(new Date());
    }
    //jwt 토큰에서 만료시간 추출함
    private Date extractExpirationDate(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}
