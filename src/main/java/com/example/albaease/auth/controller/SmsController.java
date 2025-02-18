package com.example.albaease.auth.controller;

import com.example.albaease.auth.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
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
    public ResponseEntity<String> sendSms(@RequestParam String phoneNumber, HttpSession session) {
        try {
            smsService.sendVerificationCode(phoneNumber, session);
            return ResponseEntity.ok("인증번호가 전송되었습니다.");
        } catch (CoolsmsException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    // 인증번호 확인
    @Operation(summary = "인증번호 확인")
    @PostMapping("/verify-sms")
    public ResponseEntity<String> verifySms(@RequestParam String verificationCode, HttpSession session) {
        try {
            smsService.verifyCode(verificationCode, session);
            return ResponseEntity.ok("인증이 성공적으로 완료되었습니다.");
        } catch(IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
