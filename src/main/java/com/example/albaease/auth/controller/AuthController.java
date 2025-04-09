package com.example.albaease.auth.controller;

import com.example.albaease.auth.dto.*;
import com.example.albaease.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.example.albaease.auth.exception.AuthException;
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
    @Operation(summary = "회원가입", description = "사용자가 회원가입을 요청합니다.(가입전에 아이디 중복검사, 이메일 인증 진행해야함)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "요청 오류 - 이메일 중복 체크 안 함, 이메일 인증 안 함, 비밀번호 불일치"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request) {
        // session 파라미터 추가
        authService.signup(request);
        return ResponseEntity.ok("회원 가입 성공");
    }

    //스웨거 어노테이션
    @Operation(summary = "로그인", description = "사용자가 로그인 후 JWT 토큰을 요청합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공, JWT 토큰 반환"),
            @ApiResponse(responseCode = "401", description = "인증 실패 - 이메일 또는 비밀번호 불일치")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request)  {

        String token = authService.login(request);
        return ResponseEntity.ok(token);
    }

    //아이디 중복체크
    @Operation(summary = "이메일 중복 체크",description = "회원가입 전 이메일 중복 체크를 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용 가능한 이메일입니다."),
            @ApiResponse(responseCode = "400", description = "이미 존재하는 이메일입니다.")
    })
    @PostMapping("/check-id")
    public ResponseEntity<String> checkId(@RequestBody IdCheckRequest request) {
        authService.checkIdDuplicate(request);
        return ResponseEntity.ok("사용 가능한 이메일입니다.");

    }

    //비밀번호 확인
    @Operation(summary = "현재 비밀번호 확인", description = "비밀번호 확인을 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 확인 완료"),
            @ApiResponse(responseCode = "401", description = "비밀번호 불일치")
    })
    @PostMapping("/verify-password")
    public ResponseEntity<String> verifyPassword(@RequestBody VerifyPasswordRequest request, @RequestHeader(value = "Authorization",required = false) String token) {
        authService.verifyCurrentPassword(request, token);
        return ResponseEntity.ok("비밀번호 확인 완료");
    }
    
    //비밀번호 변경
    @Operation(summary = "비밀번호 변경", description = "비밀번호 변경을 진행합니다.(변경 전에 현재 비밀번호 확인 진행해야함)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
            @ApiResponse(responseCode = "400", description = "비밀번호 체크 안 함 또는 비밀번호 불일치"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordChangeRequest request, @RequestHeader(value = "Authorization", required = false) String token){
        authService.changePassword(request, token);
        return ResponseEntity.ok("비밀번호가 성공적으로 변경되었습니다.");

    }

    // 변경할 이메일 인증 번호 요청
    @Operation(summary = "변경할 이메일 인증번호 요청", description = "이메일 변경을 위해 인증번호를 요청합니다.(요청 전에 현재 비밀번호 확인 진행해야함)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "인증 코드 발송 성공"),
            @ApiResponse(responseCode = "400", description = "현재 비밀번호 확인 안 됨 또는 이미 사용 중인 이메일"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/request-change-email")
    public ResponseEntity<String> requestChangeEmail(@RequestBody MailRequest request,
                                                     @RequestHeader(value = "Authorization", required = false) String token) {
        authService.requestEmailChange(token, request);
        return ResponseEntity.ok("변경할 이메일로 인증 코드가 발송되었습니다.");
    }

    // 인증 코드 검증 및 이메일 변경 완료
    @Operation(summary = "변경할 이메일 인증번호 입력", description = "이메일 변경을 위해 인증번호를 입력합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일 변경 성공"),
            @ApiResponse(responseCode = "400", description = "인증번호 불일치 또는 이메일 오류"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/verify-new-email")
    public ResponseEntity<String> verifyNewEmail(@RequestBody VerifyMailRequest request,
                                                 @RequestHeader(value = "Authorization", required = false) String token) {
        authService.verifyNewEmailAndChange(token, request);
        return ResponseEntity.ok("이메일이 성공적으로 변경되었습니다.");
    }

    //로그아웃


}
