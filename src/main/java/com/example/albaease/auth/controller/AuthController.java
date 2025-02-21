package com.example.albaease.auth.controller;

import com.example.albaease.auth.dto.*;
import com.example.albaease.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    //스웨거 어노테이션
    @Operation(summary = "회원가입", description = "사용자가 회원가입을 요청합니다.(가입전에 아이디 중복검사, 전화번호 인증 진행해야함)")
    @ApiResponse(responseCode = "200", description = "회원가입 성공")

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request, HttpSession session) {
//
//        // 로그 추가
//        System.out.println("All session attributes: " + Collections.list(session.getAttributeNames()));
//        System.out.println("Signup attempt. Session ID: " + session.getId());
//
//        // 세션에서 확인 - 전화번호와 함께 키 생성
//        Boolean isVerified = (Boolean) session.getAttribute("VERIFIED_PHONE_" + request.getPhoneNumber());
//        System.out.println("isPhoneVerified in session: " + isVerified);
//
//        if (isVerified == null || !isVerified) {
//            return ResponseEntity.badRequest().body("전화번호 인증을 먼저 진행해주세요.");
//        }

        // session 파라미터 추가
        authService.signup(request, session);
        return ResponseEntity.ok("회원 가입 성공");
    }

    //스웨거 어노테이션
    @Operation(summary = "로그인", description = "사용자가 로그인 후 JWT 토큰을 요청합니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공, JWT 토큰 반환")

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpSession session) {

        String token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    //아이디 중복체크
    @Operation(summary = "아이디 중복 체크",description = "회원가입 전 아이디 중복 체크를 진행합니다.")
    @PostMapping("/check-id")
    public ResponseEntity<String> checkId(@RequestBody IdCheckRequest request, HttpSession session) {
        authService.checkIdDuplicate(request, session);
        return ResponseEntity.ok("사용 가능한 ID입니다.");

    }

    //비밀번호 확인
    @Operation(summary = "현재 비밀번호 확인", description = "비밀번호 변경 전 비밀번호 확인을 진행합니다.")
    @PostMapping("/verify-password")
    public ResponseEntity<String> verifyPassword(@RequestBody VerifyPasswordRequest request, @RequestHeader(value = "Authorization",required = false) String token, HttpSession session) {
        authService.verifyCurrentPassword(request, token, session);
        return ResponseEntity.ok("비밀번호 확인 완료");
    }
    //비밀번호 변경
    @Operation(summary = "비밀번호 변경", description = "비밀번호 변경을 진행합니다.")
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequest request, @RequestHeader(value = "Authorization", required = false) String token, HttpSession session) {
        authService.changePassword(request, token, session);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");

    }


}
