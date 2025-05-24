package com.example.albaease.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePhoneNumberRequest {
    private String newPhoneNumber;
    private String verificationCode;
}
