package com.example.albaease.auth.exception;

public class SmsSendFailureException extends RuntimeException {
    public SmsSendFailureException(String message) {
        super("SMS 전송 실패: " + message);
    }
}
