package com.example.albaease.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ID 중복 예외 처리 (회원가입 시)
    @ExceptionHandler(IDAlreadyExistsException.class)
    public ResponseEntity<String> handleIDAlreadyExistsException(IDAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST); // 400 Bad Request
    }

    // 비밀번호 확인 불일치 예외 처리 (회원가입 시)
    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<String> handlePasswordMismatchException(PasswordMismatchException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST); // 400 Bad Request
    }

    // ID 또는 비밀번호 불일치 예외 처리 (로그인 시)
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED); // 401 Unauthorized
    }

    //전화번호 인증 안했을때 예외 처리(회원가입 시)
    @ExceptionHandler(PhoneVerificationRequiredException.class)
    public ResponseEntity<String> handlePhoneVerificationRequiredException(PhoneVerificationRequiredException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST); // 400 Bad Request
    }
    //아이디 중복 체크 안했을 떄 예외 처리(회원가입 시)
    @ExceptionHandler(IdDuplicationCheckRequiredException.class)
    public ResponseEntity<String> handleIdDuplicationCheckRequiredException(IdDuplicationCheckRequiredException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST); // 400 Bad Request
    }
    //sms전송 실패했을때 예외 처리(회원가입 시)
    @ExceptionHandler(SmsSendFailureException.class)
    public ResponseEntity<String> handleSmsSendFailureException(SmsSendFailureException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
    }
    //sms 인증번호 불일치 할때 예외 처리(회원가입시)
    @ExceptionHandler(InvalidVerificationCodeException.class)
    public ResponseEntity<String> handleInvalidVerificationCodeException(InvalidVerificationCodeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST); // 400 Bad Request
    }
}
