package com.example.albaease.store;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "stores")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long storeId;  // 고유 식별자 (PK)

    @Column(name = "store_code", nullable = false, unique = true)
    private String storeCode;  // 고유 매장 코드 (랜덤 생성, 숫자+문자 조합)

    @Column(name = "name", nullable = false)
    private String name;  // 매장 이름

    @Column(name = "location", nullable = false)
    private String location;  // 매장 위치

    @Column(name = "requires_approval", nullable = false)
    private Boolean requiresApproval = true;  // 사장님 권한 설정 유무 (기본값: TRUE)

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;  // 매장 생성일

    // 생성자
    public Store(String storeCode, String name, String location, Boolean requiresApproval) {
        this.storeCode = storeCode;
        this.name = name;
        this.location = location;
        this.requiresApproval = requiresApproval;
        this.createdAt = LocalDateTime.now();  // 생성 시 현재 시간 설정
    }

    // 매장 코드를 랜덤으로 생성하는 메서드 (예시)
    public static String generateStoreCode() {
        // 랜덤 코드 생성 로직 (숫자+문자 조합)
        String code = "ST" + System.currentTimeMillis() % 1000 + (int)(Math.random() * 1000);
        return code;
    }
}
