package com.example.albaease.store.controller;

import com.example.albaease.store.dto.BusinessVerificationRequestDto;
import com.example.albaease.store.dto.BusinessVerificationResponse;
import com.example.albaease.store.service.BusinessVerificationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/business")
public class BusinessVerificationController {

    private final BusinessVerificationService verificationService;

    public BusinessVerificationController(BusinessVerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping("/verify")
    public BusinessVerificationResponse verifyBusiness(@RequestBody BusinessVerificationRequestDto requestDto) {
        return verificationService.verifyBusinessNumber(requestDto.getBusinessNumber());
    }
}


