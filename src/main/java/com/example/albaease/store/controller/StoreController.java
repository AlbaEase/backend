package com.example.albaease.store.controller;

import com.example.albaease.store.dto.*;
import com.example.albaease.store.service.BusinessNumberValidator;
import com.example.albaease.store.service.StoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;
    private final BusinessNumberValidator businessNumberValidator;

    // 매장 등록 (사장님)
    @PostMapping
    public ResponseEntity<?> createStore(
            @Valid @RequestBody StoreRequestDto request,
            Authentication authentication
    ) {
        String username = authentication.getName();

        try {
            StoreResponseDto response = storeService.createStore(request, username);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // 유효하지 않은 사업자등록번호 등 → 400 에러로 반환
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // 그 외 예외 → 500 에러로 반환
            return ResponseEntity.internalServerError().body("서버 오류가 발생했습니다.");
        }
    }

    // 매장 정보 조회 (운영 or 근무 전체 매장 조회)
    @GetMapping("/me")
    public ResponseEntity<List<StoreResponseDto>> getMyStore(
            Authentication authentication
    ) {
        String username = authentication.getName();
        return ResponseEntity.ok(storeService.getMyStore(username));
    }

    // 매장 정보 수정
    @PutMapping("/{storeId}")
    public ResponseEntity<StoreResponseDto> updateStore(
            @PathVariable Long storeId,
            @Valid @RequestBody StoreUpdateRequestDto request,
            Authentication authentication
    ) {
        String username = authentication.getName();
        return ResponseEntity.ok(storeService.updateStore(storeId, request, username));
    }

    // 매장 삭제
    @DeleteMapping("/{storeId}")
    public ResponseEntity<Void> deleteStore(
            @PathVariable Long storeId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        storeService.deleteStore(storeId, username);
        return ResponseEntity.noContent().build();
    }

    // 사업자등록번호 검증 요청 DTO
    public static class BusinessNumberRequest {
        public String businessNumber;
    }

    // 사업자등록번호 유효성 검증
    @PostMapping("/validate-business-number")
    public ResponseEntity<Boolean> validateBusinessNumber(@RequestBody BusinessNumberRequest request) {
        boolean isValid = businessNumberValidator.validateBusinessNumber(request.businessNumber);
        return ResponseEntity.ok(isValid);
    }

    // 알바생이 매장코드로 매장에 등록
    @PostMapping("/join")
    public ResponseEntity<String> joinStore(
            @RequestBody StoreJoinRequestDto request,
            Authentication authentication
    ) {
        String username = authentication.getName();

        try {
            storeService.joinStore(request.getStoreCode(), username);
            return ResponseEntity.ok("매장에 등록되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("서버 오류가 발생했습니다.");
        }
    }

    // 해당 매장의 알바생 리스트 조회
    @GetMapping("/{storeId}/workers")
    public ResponseEntity<List<UserSimpleResponseDto>> getWorkersByStore(
            @PathVariable Long storeId,
            Authentication authentication) {

        String loginId = authentication.getName();
        List<UserSimpleResponseDto> workers = storeService.getWorkersByStore(storeId, loginId);
        return ResponseEntity.ok(workers);
    }

    // 해당 매장에서 알바생 삭제
    @DeleteMapping("/{storeId}/worker/{userId}")
    public void removePartTimer(@PathVariable Long storeId, @PathVariable Long userId, Authentication authentication) {
        String loginId = authentication.getName();
        storeService.removeWorker(storeId, userId, loginId);
    }

}
