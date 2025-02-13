package com.example.albaease.store.domain;

import com.example.albaease.store.dto.StoreRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Random;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "store")

public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int storeId;

    @Column(unique = true, nullable = false)
    private String storeCode;

    @Column(nullable = false)
    private String name;

    private String location;
    private Boolean requiresApproval;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Store(String storeCode, String name, String location, Boolean requiresApproval) {
        this.storeCode = generateRandomStoreCode();
        this.name = name;
        this.location = location;
        this.requiresApproval = requiresApproval;
    }
    //랜덤 코드 생성 6자리(영어 1자리 + 숫자 2자리 + 영어 2자리 + 숫자 1자리)
    private String generateRandomStoreCode() {
        Random random = new Random();
        char letter1 = (char) ('A' + random.nextInt(26)); // A-Z 중 하나
        int number1 = random.nextInt(100); // 00-99
        char letter2 = (char) ('A' + random.nextInt(26)); // A-Z 중 하나
        char letter3 = (char) ('A' + random.nextInt(26)); // A-Z 중 하나
        int number2 = random.nextInt(10); // 0-9

        return String.format("%c%02d%c%c%d", letter1, number1, letter2, letter3, number2);
    }
    //StoreCode가 비어있을 경우 자동 생성
    @PrePersist
    public void prePersist() {
        if (this.storeCode == null) {
            this.storeCode = generateRandomStoreCode();
        }
    }
    // 매장 정보 업데이트
    public void update(StoreRequestDto requestDto) {
        this.storeCode = requestDto.getStoreCode();
        this.name = requestDto.getName();
        this.location = requestDto.getLocation();
        this.requiresApproval = requestDto.getRequiresApproval();
    }
}
