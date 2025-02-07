package com.example.albaease.notification.service;

import com.example.albaease.notification.domain.entity.Notification;
import com.example.albaease.notification.domain.enums.NotificationReadStatus;
import com.example.albaease.notification.domain.enums.NotificationType;
import com.example.albaease.notification.dto.NotificationRequest;
import com.example.albaease.notification.dto.NotificationResponse;
import com.example.albaease.notification.handler.WebSocketHandler;
import com.example.albaease.notification.repository.NotificationRepository;
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

    // 웹소켓 테스트용 메서드
    @Scheduled(fixedRate = 5000)
    public void sendTestNotification() {
        // 아직 테스트 알림을 전송하지 않았고, 연결된 클라이언트가 있으면
        if (!testNotificationSent && !webSocketHandler.getClients().isEmpty()) {
            NotificationResponse notification = NotificationResponse.builder()
                    .id(1L)
                    .userId(1L)
                    .type(NotificationType.SPECIFIC_USER)  // 알림 대상 타입으로 변경
                    .readStatus(NotificationReadStatus.UNREAD)
                    .scheduleId(1L)
                    .message("테스트 알림입니다.")  // 메시지 추가
                    .createdAt(LocalDateTime.now())
                    .build();
            // 웹소켓 핸들러를 통해 알림 전송
            webSocketHandler.sendNotification(notification);
            testNotificationSent = true;  // 한 번 전송했으므로 플래그 변경
            log.info("📢 연결 성공: {}", notification);
        }
    }
    
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
