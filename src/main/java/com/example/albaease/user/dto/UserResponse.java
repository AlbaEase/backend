package com.example.albaease.user.dto;

import com.example.albaease.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse {
    private String loginId;
//    private String password;
    private String fullName;
    private String role;
    private String phoneNumber;
    private String storeName;  // 근무 매장 이름 추가

    public UserResponse(String loginId,String fullName, String role, String phoneNumber, String storeName) {// store 합친 후 수정
        this.loginId = loginId;
//        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.storeName = storeName;
    }
//    // fromEntity 메서드 추가
//    public static UserResponse fromEntity(User user, String storeName) {
//        return new UserResponse(user, storeName);
//    }
}
