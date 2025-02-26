package com.example.albaease.auth.exception;

// ID 중복 예외 (회원가입)
public class IDAlreadyExistsException extends RuntimeException {
    public IDAlreadyExistsException(String message) {
        super(message);
    }
}
