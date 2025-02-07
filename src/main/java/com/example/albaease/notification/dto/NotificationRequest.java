package com.example.albaease.notification.dto;

import com.example.albaease.notification.domain.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NotificationRequest {
    private Long userId;
    private Long scheduleId;
    private String message;
    private NotificationType type;

    // Shift 전용 필드
    private Long fromUserId;
    private Long toUserId;

    private String details;  // 수정 요청용 필드
}
