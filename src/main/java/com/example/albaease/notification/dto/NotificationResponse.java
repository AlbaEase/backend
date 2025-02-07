package com.example.albaease.notification.dto;

import com.example.albaease.notification.domain.entity.Notification;
import com.example.albaease.notification.domain.enums.NotificationReadStatus;
import com.example.albaease.notification.domain.enums.NotificationType;
import com.example.albaease.shift.domain.enums.ShiftStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NotificationResponse {
    private Long id;
    private Long userId;  // 테스트용 임시 userId
    private Long scheduleId;  // 테스트용 임시 scheduleId
    private NotificationType type;  // 알림 타입 (SPECIFIC_USER/ALL_USERS)
    private NotificationReadStatus readStatus;  // 읽음 상태 추가
    private String message;  // 알림 메시지
    private LocalDateTime createdAt;

    // 추가: 대타 요청 관련 필드
    private Long fromUserId;  // 요청을 보낸 사용자 ID
    private Long toUserId;    // 요청을 받는 사용자 ID
    private ShiftStatus status;  // 요청의 승인 상태 (PENDING/APPROVED/REJECTED)

    // 추가: 수정 요청 관련 필드
    private String details;  // 수정 요청 상세 내용

    // 알림 목록관련 필드
    private List<NotificationResponse> notifications;
    private boolean hasUnread;

    // 단일 알림 변환
    public static NotificationResponse from(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getNotification_id())
                .userId(notification.getUserId())  // 테스트용, 병합 후 수정 필요
                .scheduleId(notification.getScheduleId())  // 테스트용, 병합 후 수정 필요
                .type(notification.getRequestType())
                .readStatus(notification.getStatus())  // 읽음 상태 추가
                .message(notification.getMessage())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    // 알림 목록 변환
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
