package com.example.albaease.store.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class StoreRequestDto {
    private String storeCode;
    private String name;
    private String location;
    private Boolean requiresApproval;
}
