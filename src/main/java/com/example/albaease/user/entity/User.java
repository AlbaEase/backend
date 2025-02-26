package com.example.albaease.user.entity;

import com.example.albaease.store.domain.Store;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.time.LocalDateTime;
@Getter
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment로 user_id 생성
    @Column(name = "user_id")
    private Long userId;  // 고유 식별자 (PK)

    @Column(name = "last_name", nullable = false)
    private String lastName;  // 성

    @Column(name = "first_name", nullable = false)
    private String firstName;  // 이름

    @Column(name = "login_id", nullable = false, unique = true)
    private String loginId;  // 로그인시 사용하는 id

    @Column(name = "password", nullable = false)
    private String password;  // 비밀번호 (암호화)

    @Column(name = "phone_number")
    private String phoneNumber;  // 전화번호

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;  // 역할 (사장님/알바생)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")  // 소속된 매장의 ID (외래 키)
    private Store store;

    @Column(name = "business_number")
    private String businessNumber;  // 사업자 등록 번호 (사장님 가입 시 필요, NULL 허용)

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;  // 회원가입일

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type", nullable = false)
    private SocialType socialType;  // 소셜회원 타입 (kakao, none)

//    @Column(name = "kakao_id")
//    private String kakaoId;  // 소셜회원 식별자 (카카오톡 사용자만 필요)

    // 생성 시 자동으로 현재 시간 설정
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();  // 현재 시간을 생성 시간으로 설정
    }
    //비밀번호 변경
    public void changePassword(String newPassword, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(newPassword);
    }

    public User(String lastName, String firstName,String loginId, String password, String phoneNumber, SocialType socialType, Role role,  String businessNumber) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.loginId = loginId;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.socialType = socialType;
        this.role = role;
        this.store = store;
        this.businessNumber = businessNumber;
    }
}
