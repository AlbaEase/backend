package com.example.albaease.store.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreResponseDto {
    private String storeCode;
    private String businessNumber;
    private String name;
    private String location;
    private String ownerName;
    private String contactNumber;
    private Boolean isVerified;
    private LocalDateTime createdAt;
}
