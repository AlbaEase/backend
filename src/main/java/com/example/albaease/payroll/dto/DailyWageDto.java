package com.example.albaease.payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DailyWageDto {
    private LocalDate date;       // 근무일
    private double workingHours;  // 근무 시간 (소수점 가능)
    private int dailyWage;        // 일급
}
