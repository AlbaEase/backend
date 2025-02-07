package com.example.albaease.shift.domain.entity;

import com.example.albaease.shift.domain.enums.ShiftRequestType;
import com.example.albaease.shift.domain.enums.ShiftStatus;
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

    // api테스트용, userId, scheduleId병합 후 삭제 해야함
    private Long fromUserId;  // 요청을 보낸 사용자 ID
    private Long toUserId;    // 요청을 받는 사용자 ID
    private Long scheduleId;  // 대타 요청할 스케줄 ID

    private Long approvedBy;  // 사장님 승인 여부 (승인한 사용자 ID)

    @Enumerated(EnumType.STRING)
    private ShiftRequestType requestType;  // 요청 타입 (특정 알바생 or 전체)

    @Enumerated(EnumType.STRING)
    private ShiftStatus status;  // 요청 상태
    private LocalDateTime createdAt;  // 알림 생성 시간
    private LocalDateTime updatedAt;  // 알림 수정 시간

    @Builder
    public Shift(Long fromUserId, Long toUserId, Long scheduleId,
                 ShiftRequestType requestType) {
        this.fromUserId = fromUserId;
        this.toUserId = toUserId;
        this.scheduleId = scheduleId;
        this.requestType = requestType;
        this.status = ShiftStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    public void updateStatus(ShiftStatus status, Long approvedById) {
        this.status = status;
        this.approvedBy = approvedById;
        this.updatedAt = LocalDateTime.now();
    }
}
