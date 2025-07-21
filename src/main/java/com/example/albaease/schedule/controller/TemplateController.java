package com.example.albaease.schedule.controller;

import com.example.albaease.schedule.dto.TemplateRequest;
import com.example.albaease.schedule.dto.TemplateResponse;
import com.example.albaease.schedule.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/template")
@RequiredArgsConstructor
@Tag(name = "template-controller", description = "근무 템플릿 관련 API") // ← 컨트롤러 설명
public class TemplateController {

    private final TemplateService templateService;

    @GetMapping("/store/{storeId}")
    @Operation(
            summary = "특정 매장의 템플릿 조회",
            description = "매장의 ID(storeId)를 이용하여 해당 매장의 템플릿 목록을 조회합니다."
    )
    public ResponseEntity<List<TemplateResponse>> getTemplates(
            @Parameter(description = "매장 ID", example = "1") @PathVariable Long storeId) {
        List<TemplateResponse> templates = templateService.getTemplatesByStoreId(storeId);
        return ResponseEntity.ok(templates);
    }

    @PostMapping("/store/{storeId}")
    @Operation(
            summary = "특정 매장에 템플릿 등록",
            description = "매장 ID(storeId)와 템플릿 정보를 입력받아 해당 매장에 템플릿을 등록합니다."
    )
    public ResponseEntity<?> createTemplate(
            @Parameter(description = "매장 ID", example = "1") @PathVariable Long storeId,
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
