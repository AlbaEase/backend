package com.example.albaease.modification.dto;

import com.example.albaease.modification.domain.entity.Modification;
import com.example.albaease.modification.domain.enums.ModificationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ModificationResponse {
    private Long id;
    private Long userId;  // 테스트용 임시 userId, 병합 후 수정 필요
    private Long scheduleId;  // 테스트용 임시 scheduleId, 병합 후 수정 필요
    private String details;
    private ModificationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Entity -> DTO 변환
    public static ModificationResponse from(Modification modification) {
        return ModificationResponse.builder()
                .id(modification.getModification_id())
                .userId(modification.getUserId())  // 병합 후 수정 필요
                .scheduleId(modification.getScheduleId())  // 병합 후 수정 필요
                .details(modification.getDetails())
                .status(modification.getStatus())
                .createdAt(modification.getCreatedAt())
                .updatedAt(modification.getUpdatedAt())
                .build();
    }
}
