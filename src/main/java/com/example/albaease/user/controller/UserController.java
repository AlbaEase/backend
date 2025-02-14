//package com.example.albaease.user.controller;
//
//import com.example.albaease.user.service.UserService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/user")
//public class UserController {
//    private UserService userService;
//
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    // 전화번호 변경 요청
//    @PostMapping("/change-phone")
//    public ResponseEntity<String> changePhoneNumber(@RequestParam String loginId,
//                                                    @RequestParam String newPhoneNumber,
//                                                    @RequestParam String verificationCode) {
//        String result = userService.changePhoneNumber(loginId, newPhoneNumber, verificationCode);
//        return ResponseEntity.ok(result);
//    }
//}
