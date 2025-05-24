package com.example.albaease.store.controller;

import com.example.albaease.store.dto.StoreRequestDto;
import com.example.albaease.store.dto.StoreResponseDto;
import com.example.albaease.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.albaease.store.dto.StoreRegisterRequest;  // StoreRegisterRequest import

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    // 새로운 상점 생성
    @PostMapping
    public ResponseEntity<?> createStore(@RequestBody StoreRequestDto requestDto) {
        try {
            StoreResponseDto responseDto = new StoreResponseDto(storeService.createStore(requestDto));
            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // 모든 상점 조회
    @GetMapping
    public List<StoreResponseDto> getAllStores() {
        return storeService.getAllStores().stream()
                .map(StoreResponseDto::new)
                .collect(Collectors.toList());
    }
    // ID로 상점 조회
    @GetMapping("/user/{userId}")
    public StoreResponseDto getStoreByUserId(@PathVariable int userId) {
        return new StoreResponseDto(storeService.getStoreById(userId));
    }
    //특정 사용자가 사장으로 등록된 모든 매장 조회
    @GetMapping("/owner/{userId}")
    public List<StoreResponseDto> getStoresByOwner(@PathVariable Long userId) {
        return storeService.getStoresByOwner(userId);
    }

    // 특정 사용자가 알바생으로 등록된 모든 매장 조회
    @GetMapping("/parttimer/{userId}")
    public List<StoreResponseDto> getStoresByPartTimer(@PathVariable Long userId) {
        return storeService.getStoresByPartTimer(userId);
    }

    // 매장 정보 수정 (PUT 요청 추가)
    @PutMapping("/{id}")
    public StoreResponseDto updateStore(@PathVariable int id, @RequestBody StoreRequestDto requestDto) {
        return new StoreResponseDto(storeService.updateStore(id, requestDto));
    }

    // 매장 삭제 (DELETE 요청 추가)
    @DeleteMapping("/{id}")
    public void deleteStore(@PathVariable int id) {
        storeService.deleteStore(id);
    }

    //알바생이 스토어 코드로 등록
    @PostMapping("/register")
    public ResponseEntity<String> registerPartTimer(@RequestBody StoreRegisterRequest request) {
        storeService.registerPartTimer(request.getUserId(), request.getStoreCode());
        return ResponseEntity.ok("등록 성공");
    }

}