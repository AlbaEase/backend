package com.example.albaease.auth.controller;

import com.example.albaease.auth.dto.LoginRequest;
import com.example.albaease.auth.dto.SignupRequest;
import com.example.albaease.auth.service.AuthService;
import com.example.albaease.user.entity.Role;
import com.example.albaease.user.entity.SocialType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:3000")
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
        authService.checkIdDuplicate(id, session);
        return ResponseEntity.ok("사용 가능한 ID입니다.");

    }

    //비밀번호 확인
    @Operation(summary = "현재 비밀번호 확인")
    @PostMapping("/verify-password")
    public ResponseEntity<String> verifyPassword(@RequestParam String currentPassword, @RequestHeader(value = "Authorization",required = false) String token) {
        System.out.println("걍 로그");
        System.out.println("컨트롤러에서 토큰 확인"+ token);
        if (token == null || token.isEmpty()) {
            System.out.println("토큰이 전달되지 않음! 403 오류 가능");
            return ResponseEntity.status(403).body("토큰이 필요합니다.");
        }
        authService.verifyCurrentPassword(currentPassword, token);
        return ResponseEntity.ok("비밀번호 확인 완료");
    }
    //비밀번호 변경
    @Operation(summary = "비밀번호 변경")
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam String newPassword, @RequestParam String confirmNewPassword, @RequestHeader("Authorization") String token) {
        authService.changePassword(newPassword, confirmNewPassword, token);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");

    }


}
