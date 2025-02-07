package com.example.albaease.modification.domain.entity;

import com.example.albaease.modification.domain.enums.ModificationStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Modification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long modification_id;

    // api테스트용, 병합 후 삭제 해야함
    private Long userId;  // 알림을 받을 사용자 ID
    private Long scheduleId;  // 관련 스케줄 ID

    @Column(columnDefinition = "TEXT")
    private String details;  // 수정 요청 상세 내용

    @Enumerated(EnumType.STRING)
    private ModificationStatus status = ModificationStatus.PENDING; // 요청 상태, 기본값 대기

    private LocalDateTime createdAt;  // 생성 시간

    private LocalDateTime updatedAt;  // 수정 시간


    // userId, scheduleId필드 병합 후 수정해야함
    @Builder
    public Modification(Long userId, Long scheduleId, String details) {
        this.userId = userId;
        this.scheduleId = scheduleId;
        this.details = details;
        this.status = ModificationStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateStatus(ModificationStatus status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

}
