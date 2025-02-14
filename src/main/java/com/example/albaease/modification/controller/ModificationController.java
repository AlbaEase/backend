package com.example.albaease.modification.controller;

import com.example.albaease.modification.domain.enums.ModificationStatus;
import com.example.albaease.modification.dto.ModificationRequest;
import com.example.albaease.modification.dto.ModificationResponse;
import com.example.albaease.modification.service.ModificationService;
import com.example.albaease.notification.domain.enums.NotificationReadStatus;
import com.example.albaease.notification.domain.enums.NotificationType;
import com.example.albaease.notification.dto.NotificationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.security.Principal;

@Slf4j
@Controller
@RequestMapping("/modification")
@RequiredArgsConstructor
public class ModificationController {
    private final ModificationService modificationService;

    @PostMapping
    public ResponseEntity<ModificationResponse> createModification(
            @RequestBody ModificationRequest request,
            Principal principal) {
        String userId = principal.getName();
        request.setUserId(Long.parseLong(userId));
        return ResponseEntity.ok(modificationService.createModification(request));
    }

    @PatchMapping("/{modificationId}/status")
    public ResponseEntity<ModificationResponse> updateStatus(
            @PathVariable Long modificationId,
            @RequestParam ModificationStatus status,
            Principal principal) {
        return ResponseEntity.ok(modificationService.updateModificationStatus(modificationId, status));
    }

    @MessageMapping("/modification")
    @SendToUser("/queue/notifications")
    public NotificationResponse handleModificationRequest(
            ModificationRequest request,
            Principal principal) {
        String userId = principal.getName();
        request.setUserId(Long.parseLong(userId));
        return modificationService.handleModificationRequest(request);
    }
}
