package com.example.albaease.auth.controller;

import com.example.albaease.auth.dto.SendMailRequest;
import com.example.albaease.auth.dto.VerifyMailRequest;
import com.example.albaease.auth.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class MailController {
    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @Operation(summary = "이메일 인증요청")
    @PostMapping("/send-mail")
    public ResponseEntity<String> sendSms(@RequestBody SendMailRequest request) {
        String mailAddress = request.getMailAddress();
        try {
            mailService.sendVerificationCode(mailAddress);
            return ResponseEntity.ok("인증번호가 전송되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
    // 인증번호 확인
    @Operation(summary = "인증 번호확인")
    @PostMapping("/verify-mail")
    public ResponseEntity<String> verifySms(@RequestBody VerifyMailRequest request) {
        String mailAddress = request.getMailAddress();
        String verificationCode = request.getVerificationCode();

        if (mailAddress == null || verificationCode == null) {
            return ResponseEntity.badRequest().body("이메일 또는 인증번호가 누락되었습니다.");
        }

        try {

            mailService.verifyCode(mailAddress, verificationCode);
            return ResponseEntity.ok("인증이 성공적으로 완료되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
