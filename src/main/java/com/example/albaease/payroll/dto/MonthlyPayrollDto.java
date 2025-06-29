package com.example.albaease.payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class MonthlyPayrollDto {
    private int monthlyTotalWage;         // 총 월급
    private List<DailyWageDto> dailyWageList; // 일급 리스트
}
