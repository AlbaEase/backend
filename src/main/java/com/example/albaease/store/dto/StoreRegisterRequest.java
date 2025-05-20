package com.example.albaease.store.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class StoreRegisterRequest {
    private Long userId;   // 사용자 ID
    private String storeCode;  // 스토어 코드

    public StoreRegisterRequest(Long userId, String storeCode) {
        this.userId = userId;
        this.storeCode = storeCode;
    }
}
