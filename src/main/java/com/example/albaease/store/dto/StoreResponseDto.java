package com.example.albaease.store.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreResponseDto {
<<<<<<< HEAD
    private Long id; // 엔티티의 id 추가
=======
    private Long storeId;
>>>>>>> 8b48bf6b6681eaede7440413d4ecd8899f340996
    private String storeCode;
    private String businessNumber;
    private String name;
    private String location;

    // 엔티티의 승인 여부 필드 추가
    private Boolean requireApproval;

    private LocalDateTime createdAt;

}
