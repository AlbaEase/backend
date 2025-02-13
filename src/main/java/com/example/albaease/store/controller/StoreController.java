package com.example.albaease.store.controller;

import com.example.albaease.store.dto.StoreRequestDto;
import com.example.albaease.store.dto.StoreResponseDto;
import com.example.albaease.store.service.StoreService;
import lombok.RequiredArgsConstructor;
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
    public StoreResponseDto createStore(@RequestBody StoreRequestDto requestDto) {
        return new StoreResponseDto(storeService.createStore(requestDto));
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
}