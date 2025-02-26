package com.example.albaease.auth.exception;

public class PhoneVerificationRequiredException extends RuntimeException {
    public PhoneVerificationRequiredException(String message) {
        super(message);
    }
}
