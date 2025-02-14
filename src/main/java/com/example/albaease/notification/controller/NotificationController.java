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

@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/me")
    public ResponseEntity<NotificationResponse> getNotifications(Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        NotificationResponse response = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/me/{notificationId}")
    public ResponseEntity<Void> deleteNotification(
            Principal principal,
            @PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteAllNotifications(Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        notificationService.deleteAllNotifications(userId);
        return ResponseEntity.noContent().build();
    }

    @MessageMapping("/notification")
    @SendToUser("/queue/notifications")
    public NotificationResponse handleNotification(
            Principal principal,
            NotificationRequest request) {
        Long userId = Long.parseLong(principal.getName());
        request.setUserId(userId);  // userId를 Principal에서 가져온 값으로 설정
        return notificationService.createNotification(request);
    }

    @MessageMapping("/subscribe")
    public void subscribe(StompHeaderAccessor headerAccessor, Principal principal) {
        String userId = principal.getName();
        headerAccessor.getSessionAttributes().put("userId", userId);
    }
}
