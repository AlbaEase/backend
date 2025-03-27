package com.example.albaease.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMailRequest {
    @Schema(example = "user@naver.com")
    private String mailAddress;
}
