package com.example.albaease.notification.dto;

import com.example.albaease.notification.domain.entity.Notification;
import com.example.albaease.notification.domain.enums.NotificationStatus;
import com.example.albaease.notification.domain.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class NotificationResponse {
    private Long id;
    private Long userId;
    private NotificationType type;
    private NotificationStatus status;
    private LocalDateTime createdAt;

    // 알림 목록관련 필드
    private List<NotificationResponse> notifications;
    private boolean hasUnread;

    // 단일 알림 
    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getNotification_id())
                .userId(notification.getUserId())
                .type(notification.getRequestType())
                .status(notification.getStatus())
                .createdAt(notification.getCreated_at())
                .build();
    }

    // 알림 목록 
    public static NotificationResponse ofList(List<Notification> notifications, boolean hasUnread) {
        List<NotificationResponse> responseList = notifications.stream()
                .map(NotificationResponse::from)
                .collect(Collectors.toList());

        return NotificationResponse.builder()
                .notifications(responseList)
                .hasUnread(hasUnread)
                .build();
    }
}
