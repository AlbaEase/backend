package com.example.albaease.notification.domain.entity;

import com.example.albaease.notification.domain.enums.RequestStatus;
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
    private RequestStatus status;  // 요청 상태

    private LocalDateTime created_at;  // 생성 시간
    private LocalDateTime updated_at;  // 수정 시간

    // userId, scheduleId 병합 후 수정해야함
    @Builder
    public Modification(Long userId, Long scheduleId, String details) {
        this.userId = userId;
        this.scheduleId = scheduleId;
        this.details = details;
        this.status = RequestStatus.PENDING;
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }

    public void updateStatus(RequestStatus status) {
        this.status = status;
        this.updated_at = LocalDateTime.now();
    }
}
