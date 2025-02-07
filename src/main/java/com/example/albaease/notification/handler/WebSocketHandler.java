package com.example.albaease.notification.handler;

import com.example.albaease.notification.domain.enums.NotificationType;
import com.example.albaease.notification.dto.NotificationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketHandler {

    private final SimpMessageSendingOperations messagingTemplate;

    // 연결된 세션 관리를 위한 Map (userId -> 세션 관리)
    private static final Map<String, String> CLIENTS = new ConcurrentHashMap<>();

    public Map<String, String> getClients() {
        return CLIENTS;
    }

    // 알림 전송 메서드 - 대상 타입에 따라 전송 방식 분기
    public void sendNotification(NotificationResponse notification) {
        // 전체 알바생 대상 알림
        if (notification.getType() == NotificationType.ALL_USERS) {
            messagingTemplate.convertAndSend(
                    "/topic/notifications",
                    notification
            );
        }
        // 특정 알바생 대상 알림
        else {
            messagingTemplate.convertAndSendToUser(
                    notification.getUserId().toString(),
                    "/queue/notifications",
                    notification
            );
        }
    }

    // 모든 알림 삭제 시 전송 메서드
    public void sendNotificationDeleteAll(Long userId) {
        messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/queue/notifications/delete-all",
                "모든 알림이 삭제되었습니다."
        );
    }

    // 대타 요청 상태 변경 알림 전송
    public void sendShiftStatusUpdate(Long userId, String message) {
        messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/queue/notifications/shift-status",
                message
        );
    }

    // 수정 요청 상태 변경 알림 전송
    public void sendModificationStatusUpdate(Long userId, String message) {
        messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/queue/notifications/modification-status",
                message
        );
    }

    // 웹소켓 연결 시 실행되는 이벤트 리스너
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        if (headerAccessor.getSessionAttributes() == null) {
            headerAccessor.setSessionAttributes(new HashMap<>());
        }
        headerAccessor.getSessionAttributes().put("userId", "1");
        CLIENTS.put("1", headerAccessor.getSessionId());
        log.info("✅ 새로운 웹소켓 연결: 사용자 ID = 1");
    }

    // 웹소켓 연결 해제 시 실행되는 이벤트 리스너
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        if (headerAccessor.getSessionAttributes() == null) {
            log.warn("웹소켓 연결 해제 실패: 세션 속성이 없음");
            return;
        }

        Object userIdObj = headerAccessor.getSessionAttributes().get("userId");
        if (userIdObj == null) {
            log.warn("웹소켓 연결 해제 실패: 사용자 ID 없음");
            return;
        }

        String userId = userIdObj.toString();
        CLIENTS.remove(userId); // 사용자의 세션 삭제
        log.info("❌ 웹소켓 연결 해제: 사용자 ID = " + userId);
    }

    // 중복된 메서드 제거 (SessionConnectEvent는 불필요)
}
