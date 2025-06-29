package com.example.albaease.payroll.dto;

import lombok.Getter;

@Getter
public class WageUpdateRequest {
    private Long storeId;
    private Integer hourlyWage; // 수정할 시급
}
