package com.example.albaease.modification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ModificationRequest {
    private Long userId;  // 테스트용 임시 userId, 병합 후 수정 필요
    private Long scheduleId;  // 테스트용 임시 scheduleId, 병합 후 수정 필요
    private String details;  // 수정 요청 상세 내용
}
