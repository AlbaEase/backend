package com.example.albaease.auth.controller;

import com.example.albaease.auth.service.SmsService;
import jakarta.servlet.http.HttpSession;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class SmsController {
    private final SmsService smsService;

    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }
    // 전화번호 인증 요청
    @PostMapping("/send-sms")
    public ResponseEntity<String> sendSms(@RequestParam String phoneNumber, HttpSession session) {
        // 전화번호를 세션에 저장(인증번호 확인시 필요)
        session.setAttribute("phoneNumber", phoneNumber);
        try {
            smsService.sendVerificationCode(phoneNumber);
            return ResponseEntity.ok("인증번호가 전송되었습니다.");
        } catch (CoolsmsException e) {
            return ResponseEntity.status(500).body("SMS 전송 실패: " + e.getMessage());
        }
    }

    // 인증번호 확인
    @PostMapping("/verify-sms")
    public ResponseEntity<String> verifySms(@RequestParam String verificationCode, HttpSession session) {
        //세션에서 전화번호 가져옴
        String phoneNumber = (String) session.getAttribute("phoneNumber");

        if (phoneNumber == null) {
            return ResponseEntity.status(400).body("전화번호가 세션에 없습니다.");
        }

        boolean isValid = smsService.verifyCode(phoneNumber, verificationCode, session);
        if (isValid) {
            return ResponseEntity.ok("인증번호가 일치합니다.");
        } else {
            return ResponseEntity.status(400).body("인증번호가 일치하지 않습니다.");
        }
    }
}
