package com.example.albaease.user.controller;

import com.example.albaease.auth.CustomUserDetails;
import com.example.albaease.user.dto.UserResponse;
import com.example.albaease.user.entity.User;
import com.example.albaease.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 개인정보 확인
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        // 서비스에서 사용자 정보 가져오기
        UserResponse userResponse = userService.getCurrentUser(userDetails);
        // userResponse가 제대로 값이 들어있는지 확인
        System.out.println("userResponse: " + userResponse.getLoginId());
        System.out.println("userResponse: " + userResponse.getFullName());
        System.out.println("userResponse: " + userResponse.getRole());
        System.out.println("userResponse: " + userResponse.getPhoneNumber());
        System.out.println("userResponse: " + userResponse.getStoreName());

         // 성공적으로 사용자 정보를 가져왔으면 반환
        return ResponseEntity.ok(userResponse);
    }

//    // 전화번호 변경 요청
//    @PostMapping("/change-phone")
//    public ResponseEntity<String> changePhoneNumber(@RequestParam String loginId,
//                                                    @RequestParam String newPhoneNumber,
//                                                    @RequestParam String verificationCode) {
//        String result = userService.changePhoneNumber(loginId, newPhoneNumber, verificationCode);
//        return ResponseEntity.ok(result);
//    }

}
