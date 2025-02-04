package com.example.albaease.notification.dto;

import com.example.albaease.notification.domain.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationRequest {
    private Long userId;
    private NotificationType type;
}
