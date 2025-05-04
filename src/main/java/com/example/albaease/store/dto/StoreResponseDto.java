package com.example.albaease.store.dto;

import com.example.albaease.store.domain.Store;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class StoreResponseDto {
    private final int storeId;
    private final String storeCode;
    private final String name;
    private final String location;
    private final Boolean requiresApproval;
    private final LocalDateTime createdAt;
    private final String businessNumber; // 추가
    private final String ownerName;      // 추가
    private final String startDate;      // 추가

    public StoreResponseDto(Store store) {
        this.storeId = store.getStoreId();                   // 추가
        this.storeCode = store.getStoreCode();
        this.name = store.getName();
        this.location = store.getLocation();
        this.requiresApproval = store.getRequiresApproval();
        this.createdAt = store.getCreatedAt();               // 추가
        this.businessNumber = store.getBusinessNumber();     // 추가
        this.ownerName = store.getOwnerName();               // 추가
        this.startDate = store.getStartDate();               // 추가
    }
}
