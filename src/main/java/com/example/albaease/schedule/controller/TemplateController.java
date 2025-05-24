package com.example.albaease.schedule.controller;

import com.example.albaease.schedule.dto.TemplateRequest;
import com.example.albaease.schedule.dto.TemplateResponse;
import com.example.albaease.schedule.service.TemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/template")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    // 특정 매장의 템플릿 조회 API
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<TemplateResponse>> getTemplates(@PathVariable Long storeId) {
        List<TemplateResponse> templates = templateService.getTemplatesByStoreId(storeId);
        return ResponseEntity.ok(templates);
    }

    // 특정 매장에 템플릿 등록
// TemplateController.java
    @PostMapping("/store/{storeId}")
    public ResponseEntity<?> createTemplate(
            @PathVariable Long storeId,
            @RequestBody TemplateRequest templateRequest) {
        try {
            TemplateResponse createdTemplate = templateService.createTemplate(storeId, templateRequest);
            return ResponseEntity.ok(createdTemplate);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
        }
    }

}
