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

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ScheduleRepository scheduleRepository;
    private final SimpMessageSendingOperations messagingTemplate;
    private final WebSocketHandler webSocketHandler;

    @Transactional
    public NotificationResponse createNotification(NotificationRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        Schedule schedule = null;
        if (request.getScheduleId() != null) {
            schedule = scheduleRepository.findById(request.getScheduleId())
                    .orElseThrow(() -> new EntityNotFoundException("스케줄을 찾을 수 없습니다."));
        }

        Notification notification = Notification.builder()
                .user(user)
                .schedule(schedule)
                .message(request.getMessage())
                .requestType(request.getType())
                .status(NotificationReadStatus.UNREAD)
                .build();

        Notification savedNotification = notificationRepository.save(notification);
        NotificationResponse response = NotificationResponse.from(savedNotification);

        messagingTemplate.convertAndSendToUser(
                user.getId().toString(),
                "/queue/notifications",
                response
        );

        return response;
    }

    @Transactional
    public NotificationResponse createShiftNotification(NotificationRequest request, ShiftResponse shiftResponse) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        Schedule schedule = null;
        if (request.getScheduleId() != null) {
            schedule = scheduleRepository.findById(request.getScheduleId())
                    .orElseThrow(() -> new EntityNotFoundException("스케줄을 찾을 수 없습니다."));
        }

        Notification notification = Notification.builder()
                .user(user)
                .schedule(schedule)
                .message(request.getMessage())
                .requestType(request.getType())
                .status(NotificationReadStatus.UNREAD)
                .build();

        Notification savedNotification = notificationRepository.save(notification);

        NotificationResponse response = NotificationResponse.builder()
                .id(savedNotification.getNotification_id())
                .userId(user.getId())
                .type(request.getType())
                .readStatus(NotificationReadStatus.UNREAD)
                .message(request.getMessage())
                .scheduleId(schedule != null ? schedule.getId() : null)
                .fromUserId(request.getFromUserId())
                .toUserId(request.getToUserId())
                .createdAt(savedNotification.getCreatedAt())
                .shiftStatus(shiftResponse.getStatus())
                .build();

        webSocketHandler.sendNotification(response);

        return response;
    }

    @Transactional
    public NotificationResponse createModificationNotification(NotificationRequest request, ModificationResponse modificationResponse) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        Schedule schedule = null;
        if (request.getScheduleId() != null) {
            schedule = scheduleRepository.findById(request.getScheduleId())
                    .orElseThrow(() -> new EntityNotFoundException("스케줄을 찾을 수 없습니다."));
        }

        Notification notification = Notification.builder()
                .user(user)
                .schedule(schedule)
                .message(request.getMessage())
                .requestType(request.getType())
                .status(NotificationReadStatus.UNREAD)
                .build();

        Notification savedNotification = notificationRepository.save(notification);

        NotificationResponse response = NotificationResponse.builder()
                .id(savedNotification.getNotification_id())
                .userId(user.getId())
                .type(request.getType())
                .readStatus(NotificationReadStatus.UNREAD)
                .message(request.getMessage())
                .scheduleId(schedule != null ? schedule.getId() : null)
                .details(request.getDetails())
                .createdAt(savedNotification.getCreatedAt())
                .modificationStatus(modificationResponse.getStatus())
                .build();

        webSocketHandler.sendNotification(response);

        return response;
    }

    public NotificationResponse getUserNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        boolean hasUnread = notifications.stream()
                .anyMatch(notification -> notification.getStatus() == NotificationReadStatus.UNREAD);

        return NotificationResponse.ofList(notifications, hasUnread);
    }

    @Transactional
    public void deleteNotification(Long notificationId) {
        notificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("알림을 찾을 수 없습니다."));
        notificationRepository.deleteById(notificationId);
    }

    @Transactional
    public void deleteAllNotifications(Long userId) {
        notificationRepository.deleteByUserId(userId);
    }
}
