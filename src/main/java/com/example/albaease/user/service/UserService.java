//package com.example.albaease.user.service;
//
//import com.example.albaease.auth.service.SmsService;
//import com.example.albaease.user.entity.User;
//import com.example.albaease.user.repository.UserRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//@Service
//public class UserService {
//
//    private final UserRepository userRepository;
//    private final SmsService smsService;
//
//    public UserService(UserRepository userRepository, SmsService smsService) {
//        this.userRepository = userRepository;
//        this.smsService = smsService;
//    }
//
//    // 전화번호 변경 메소드
//    @Transactional
//    public String changePhoneNumber(String loginId, String newPhoneNumber, String verificationCode) {
//        // 전화번호 인증 확인
//        boolean isVerified = smsService.verifyCode(newPhoneNumber, verificationCode);
//        if (!isVerified) {
//            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
//        }
//
//        // 로그인 아이디로 사용자 찾기
//        User user = userRepository.findByLoginId(loginId)
//                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
//
//        // 전화번호 변경
//        user.setPhoneNumber(newPhoneNumber);
//        userRepository.save(user); // 전화번호 업데이트
//
//        return "전화번호가 성공적으로 변경되었습니다.";
//    }
//
//
//}
