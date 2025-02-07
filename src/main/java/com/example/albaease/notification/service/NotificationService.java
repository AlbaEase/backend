package com.example.albaease.notification.service;

import com.example.albaease.modification.dto.ModificationResponse;
import com.example.albaease.notification.domain.entity.Notification;
import com.example.albaease.notification.domain.enums.NotificationReadStatus;
import com.example.albaease.notification.domain.enums.NotificationType;
import com.example.albaease.notification.dto.NotificationRequest;
import com.example.albaease.notification.dto.NotificationResponse;
import com.example.albaease.notification.handler.WebSocketHandler;
import com.example.albaease.notification.repository.NotificationRepository;
import com.example.albaease.shift.dto.ShiftResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessageSendingOperations messagingTemplate;
    private final WebSocketHandler webSocketHandler;

    // 테스트 알림이 최초 1회만 전송되도록 하는 플래그
    // 병합 후 삭제필요
    private boolean testNotificationSent = false;
    
    // 알림 생성
    @Transactional
    public NotificationResponse createNotification(NotificationRequest request) {
        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .requestType(request.getType())
                .build();

        // Notification 저장
        Notification savedNotification = notificationRepository.save(notification);
        
        NotificationResponse response = NotificationResponse.from(savedNotification);

        // 웹소켓으로 실시간 알림 전송 
        messagingTemplate.convertAndSendToUser(
                request.getUserId().toString(),
                "/queue/notifications",
                response
        );

        // 저장된 알림을 반환
        return response;
    }

    // 대타 요청 생성
    @Transactional
    public NotificationResponse createShiftNotification(NotificationRequest request, ShiftResponse shiftResponse) {
        // 1. Notification 엔티티 생성 및 저장
        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .scheduleId(request.getScheduleId())
                .message(request.getMessage())
                .requestType(request.getType())
                .status(NotificationReadStatus.UNREAD)
                .build();

        Notification savedNotification = notificationRepository.save(notification);

        // 2. Shift 전용 응답 생성 (detail 필드 제외)
        NotificationResponse response = NotificationResponse.builder()
                .id(savedNotification.getNotification_id())
                .userId(request.getUserId())
                .type(request.getType())
                .readStatus(NotificationReadStatus.UNREAD)
                .message(request.getMessage())
                .scheduleId(request.getScheduleId())
                .fromUserId(request.getFromUserId())
                .toUserId(request.getToUserId())
                .createdAt(savedNotification.getCreatedAt())
                .shiftStatus(shiftResponse.getStatus())
                .build();

        // 3. 웹소켓으로 전송
        webSocketHandler.sendNotification(response);

        return response;
    }

    // 수정 요청 생성
    @Transactional
    public NotificationResponse createModificationNotification(NotificationRequest request, ModificationResponse modificationResponse) {
        // 1. Notification 엔티티 생성 및 저장
        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .scheduleId(request.getScheduleId())
                .message(request.getMessage())
                .requestType(request.getType())
                .status(NotificationReadStatus.UNREAD)
                .build();

        Notification savedNotification = notificationRepository.save(notification);

        // 2. Modification 전용 응답 생성 (fromUserId, toUserId 제외)
        NotificationResponse response = NotificationResponse.builder()
                .id(savedNotification.getNotification_id())
                .userId(request.getUserId())
                .type(request.getType())
                .readStatus(NotificationReadStatus.UNREAD)
                .message(request.getMessage())
                .scheduleId(request.getScheduleId())
                .details(request.getDetails())
                .createdAt(savedNotification.getCreatedAt())
                .modificationStatus(modificationResponse.getStatus())
                .build();

        // 3. 웹소켓으로 전송
        webSocketHandler.sendNotification(response);

        return response;
    }


    // 사용자의 알림 목록 조회 + 읽지 않은 알림 존재 여부
    public NotificationResponse getUserNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        boolean hasUnread = notifications.stream()
                .anyMatch(notification -> notification.getStatus() == NotificationReadStatus.UNREAD);

        return NotificationResponse.ofList(notifications, hasUnread);
    }

    // 단일 알림 삭제
    @Transactional
    public void deleteNotification(Long notificationId) {
        notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("알림을 찾을 수 없습니다."));
        notificationRepository.deleteById(notificationId);
    }

    // 모든 알림 삭제
    @Transactional
    public void deleteAllNotifications(Long userId) {
        notificationRepository.deleteByUserId(userId);
    }
}
