package com.example.albaease.notification.controller;

import com.example.albaease.notification.dto.NotificationRequest;
import com.example.albaease.notification.dto.NotificationResponse;
import com.example.albaease.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

// 임시로 userId를 파라미터로 받음, 병합 후 파라미터에 Principal객체 추가 해야함
@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    
    // 알림 목록 조회
    @GetMapping("/me")
    public ResponseEntity<NotificationResponse> getNotifications(@RequestParam Long userId) {
        NotificationResponse response = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(response);
    }
   
    // 알림 개별 삭제
    @DeleteMapping("/me/{notificationId}")
    public ResponseEntity<Void> deleteNotification(@RequestParam Long userId,
                                                   @PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.noContent().build();
    }
    
    // 알림 전체 삭제
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteAllNotifications(@RequestParam Long userId) {
        notificationService.deleteAllNotifications(userId);
        return ResponseEntity.noContent().build();
    }

    @MessageMapping("/notification")  // /app/notification으로 클라이언트에서 접근
    @SendToUser("/queue/notifications")
    public NotificationResponse handleNotification(Principal principal, NotificationRequest request) {  // request 파라미터 추가
        // principal 대신 request에서 userId를 받아서 사용
        Long userId = request.getUserId();  // 테스트를 위해 임시로 request에서 userId 사용
        return notificationService.createNotification(request);
    }

    @MessageMapping("/subscribe")
    public void subscribe(StompHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("userId", "1"); // 테스트용 임시 userId
    }

}
