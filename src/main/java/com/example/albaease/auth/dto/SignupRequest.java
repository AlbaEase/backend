package com.example.albaease.auth.dto;

import com.example.albaease.user.Role;
import com.example.albaease.user.SocialType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    private SocialType socialType;
    private String lastName;
    private String firstName;
    private String id;
    private String password;
    private String confirmPassword; // 비밀번호 확인
    private String phoneNumber;
    private Role role;
}
