package com.example.albaease.notification.domain.entity;

import com.example.albaease.notification.domain.enums.RequestStatus;
import com.example.albaease.notification.domain.enums.RequestType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long request_id;

    // API 테스트용 - User, Schedule 엔티티 없이 ID만 저장 (병합 후 삭제해야함)
    private Long fromUserId;  // 요청을 보낸 사용자 ID
    private Long toUserId;    // 요청을 받는 사용자 ID
    private Long scheduleId;  // 대타 요청할 스케줄 ID
    private Long approvedBy;  // 사장님 승인 여부 (승인한 사용자 ID)

    @Enumerated(EnumType.STRING)
    private RequestType requestType;  // 요청 타입 (특정 알바생 or 전체)

    @Enumerated(EnumType.STRING)
    private RequestStatus status;  // 요청 상태

    private LocalDateTime created_at;  // 알림 생성 시간
    private LocalDateTime updated_at;  // 알림 수정 시간

    @Builder
    public Shift(Long fromUserId, Long toUserId, Long scheduleId,
                 RequestType requestType) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.scheduleId = scheduleId;
        this.requestType = requestType;
        this.status = RequestStatus.PENDING;
        this.created_at = LocalDateTime.now();
        this.updated_at = LocalDateTime.now();
    }

    public void updateStatus(RequestStatus status, Long approvedById) {
        this.status = status;
        this.approvedBy = approvedById;
        this.updated_at = LocalDateTime.now();
    }
}
