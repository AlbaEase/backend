package com.example.albaease.auth.exception;

//id 또는 비번 불일치(로그인)
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
