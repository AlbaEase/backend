package com.example.albaease.auth.exception;

//비번 확인시 불일치(회원가입)
public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException(String message) {
        super(message);
    }

}
