package com.example.albaease.notification.domain.entity;

import com.example.albaease.notification.domain.enums.NotificationStatus;
import com.example.albaease.notification.domain.enums.NotificationType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notification_id;

    // api테스트용, 병합 후 삭제 해야함
    private Long userId;  // 알림을 받을 사용자 ID
    private Long scheduleId;  // 관련 스케줄 ID

    @Enumerated(EnumType.STRING)
    private NotificationStatus status;  // 읽음 여부 상태

    @Enumerated(EnumType.STRING)
    private NotificationType requestType;  // 알림 타입 (특정 알바생 or 전체 요청)

    private LocalDateTime created_at;  // 생성 시간

    @Builder
    public Notification(Long userId, Long scheduleId,
                        NotificationType requestType) {
        this.userId = userId;
        this.scheduleId = scheduleId;
        this.status = NotificationStatus.UNREAD;
        this.requestType = requestType;
        this.created_at = LocalDateTime.now();
    }

    public void markAsRead() {
        this.status = NotificationStatus.READ;
    }
}
