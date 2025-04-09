package com.example.albaease.user.service;

import com.example.albaease.auth.CustomUserDetails;
import com.example.albaease.auth.CustomUserDetailsService;

import com.example.albaease.user.dto.UserResponse;
import com.example.albaease.user.entity.User;
import com.example.albaease.user.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final CustomUserDetailsService customUserDetailsService;

    public UserService(UserRepository userRepository,  CustomUserDetailsService customUserDetailsService) {
        this.userRepository = userRepository;
        this.customUserDetailsService = customUserDetailsService;
    }


    //유저정보 메서드
    public UserResponse getCurrentUser(CustomUserDetails userDetails) {
        String storeName = "임시 매장 이름";

//        System.out.println("유저정보 출력 " + userDetails.getLoginId());
//        System.out.println("유저정보 출력 " + userDetails.getFullName());
//        System.out.println("유저정보 출력 " + userDetails.getRole());
//        System.out.println("유저정보 출력 " + userDetails.getPhoneNumber());
//        System.out.println("유저정보 출력 " + storeName);

        return new UserResponse(
                userDetails.getLoginId(),
                userDetails.getFullName(),
                userDetails.getRole(),
                userDetails.getStoreName()
        );
    }


}
