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

@Slf4j
@Controller
@RequestMapping("/modification")  // 테스트용 임시 URL
@RequiredArgsConstructor
public class ModificationController {
    private final ModificationService modificationService;

    // 수정 요청 생성 (테스트용)
    @PostMapping
    public ResponseEntity<ModificationResponse> createModification(
            @RequestBody ModificationRequest request) {
        return ResponseEntity.ok(modificationService.createModification(request));
    }

    // 수정 요청 상태 업데이트 (승인/거절)
    @PatchMapping("/{modificationId}/status")
    public ResponseEntity<ModificationResponse> updateStatus(
            @PathVariable Long modificationId,
            @RequestParam ModificationStatus status) {
        return ResponseEntity.ok(modificationService.updateModificationStatus(modificationId, status));
    }


    @MessageMapping("/modification")
    @SendToUser("/queue/notifications")
    public NotificationResponse handleModificationRequest(ModificationRequest request) {
        log.info("Received modification request: {}", request);
        return modificationService.handleModificationRequest(request);
    }
}
