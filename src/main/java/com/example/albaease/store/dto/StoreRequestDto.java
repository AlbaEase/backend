package com.example.albaease.store.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StoreRequestDto {
    private String storeCode;
    private String name;
    private String location;
    private Boolean requiresApproval;
    private String businessNumber;
    private final String startDate = "2000-01-01"; // 추가된 필드
    private String ownerName; // 추가된 필드

}
