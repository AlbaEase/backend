package com.example.albaease.auth.controller;

import com.example.albaease.auth.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class SmsController {
    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }
    // 전화번호 인증 요청
    @Operation(summary = "전화번호 인증요청")
    @PostMapping("/send-sms")
    public ResponseEntity<String> sendSms(@RequestBody Map<String, String> request, HttpSession session) {
        String phoneNumber = request.get("phoneNumber");
        try {
            smsService.sendVerificationCode(phoneNumber, session);
            return ResponseEntity.ok("인증번호가 전송되었습니다.");
        } catch (CoolsmsException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    // 인증번호 확인
    @Operation(summary = "인증 번호확인")
    @PostMapping("/verify-sms")
    public ResponseEntity<String> verifySms(@RequestBody Map<String, String> request) {
        // requestBody에서 JSON key "phoneNumber", "verificationCode" 추출
        String phoneNumber = request.get("phoneNumber");
        String verificationCode = request.get("verificationCode");

        // phoneNumber나 verificationCode가 null이라면 잘못된 요청
        if (phoneNumber == null || verificationCode == null) {
            return ResponseEntity.badRequest().body("전화번호 또는 인증번호가 누락되었습니다.");
        }

        try {
            // 수정된 서비스 메서드(verifyCode) 호출
            smsService.verifyCode(phoneNumber, verificationCode);
            return ResponseEntity.ok("인증이 성공적으로 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            // 인증번호가 일치하지 않거나 만료되었을 때
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
