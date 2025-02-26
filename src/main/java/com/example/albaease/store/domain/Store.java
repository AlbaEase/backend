package com.example.albaease.store.domain;

import com.example.albaease.store.dto.StoreRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "store")

public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private int storeId;

    @Column(unique = true, nullable = false)
    private String storeCode;

    @Column(nullable = false)
    private String name;

    private String location;
    private Boolean requiresApproval;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Store(String storeCode, String name, String location, Boolean requiresApproval) {
        this.storeCode = storeCode;  // 랜덤코드는 서비스에서 생성
        this.name = name;
        this.location = location;
        this.requiresApproval = requiresApproval;
    }

    // 매장 정보 업데이트
    public void update(StoreRequestDto requestDto) {
        this.storeCode = requestDto.getStoreCode();
        this.name = requestDto.getName();
        this.location = requestDto.getLocation();
        this.requiresApproval = requestDto.getRequiresApproval();
    }
}
