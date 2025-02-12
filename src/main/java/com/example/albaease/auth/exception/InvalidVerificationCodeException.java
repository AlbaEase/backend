package com.example.albaease.auth.exception;

public class InvalidVerificationCodeException extends RuntimeException {
    public InvalidVerificationCodeException(String message) {
        super("인증번호 불일치: "+message);
    }
}
