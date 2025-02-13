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

    public StoreResponseDto(Store store) {
        this.storeId = store.getStoreId();
        this.storeCode = store.getStoreCode();
        this.name = store.getName();
        this.location = store.getLocation();
        this.requiresApproval = store.getRequiresApproval();
        this.createdAt = store.getCreatedAt();
    }
}
