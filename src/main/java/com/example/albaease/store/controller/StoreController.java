package com.example.albaease.store.controller;

import com.example.albaease.store.dto.StoreRequestDto;
import com.example.albaease.store.dto.StoreResponseDto;
import com.example.albaease.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/{id}")
    public StoreResponseDto getStoreById(@PathVariable int id) {
        return new StoreResponseDto(storeService.getStoreById(id));
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
}