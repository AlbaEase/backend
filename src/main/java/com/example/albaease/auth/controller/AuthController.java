package com.example.albaease.auth.controller;

import com.example.albaease.auth.dto.LoginRequest;
import com.example.albaease.auth.dto.SignupRequest;
import com.example.albaease.auth.exception.IDAlreadyExistsException;
import com.example.albaease.auth.service.AuthService;
import com.example.albaease.user.entity.Role;
import com.example.albaease.user.entity.SocialType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    //스웨거 어노테이션
    @Operation(summary = "회원가입", description = "사용자가 회원가입을 요청합니다.")
    @ApiResponse(responseCode = "200", description = "회원가입 성공")

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestParam SocialType socialType,
                                         @RequestParam String lastName,
                                         @RequestParam String firstName,
                                         @RequestParam String id,
                                         @RequestParam String password,
                                         @RequestParam String confirmPassword,
                                         @RequestParam String phoneNumber,
                                         @RequestParam Role role,
                                         HttpSession session) {
        // SignupRequest 객체 생성
        SignupRequest request = new SignupRequest(socialType, lastName, firstName, id, password, confirmPassword, phoneNumber, role);

        // 회원가입 서비스 호출
        authService.signup(request, session);
        return ResponseEntity.ok("회원가입 성공");
    }

    //스웨거 어노테이션
    @Operation(summary = "로그인", description = "사용자가 로그인 후 JWT 토큰을 요청합니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공, JWT 토큰 반환")
//    @ApiResponse(responseCode = "401", description = "잘못된 ID 또는 비밀번호")

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String id, @RequestParam String password) {
        // 로그인 요청을 AuthService에 전달
        LoginRequest request = new LoginRequest(id, password);
        String token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    //아이디 중복체크
    @Operation(summary = "아이디 중복체크")
    @PostMapping("/check-id")
    public ResponseEntity<String> checkId(@RequestParam String id, HttpSession session) {
        try {
            authService.checkIdDuplicate(id, session);
            return ResponseEntity.ok("사용 가능한 ID입니다.");
        } catch (IDAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
