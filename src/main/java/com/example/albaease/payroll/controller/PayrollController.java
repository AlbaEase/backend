package com.example.albaease.payroll.controller;

import com.example.albaease.payroll.dto.MonthlyPayrollDto;
import com.example.albaease.payroll.dto.WageUpdateRequest;
import com.example.albaease.payroll.service.PayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payroll")
public class PayrollController {
    private final PayrollService payrollService;
    // 내 급여 조회
    @GetMapping("/me")
    public MonthlyPayrollDto getMyPayroll(@RequestParam int year, @RequestParam int month) {
        return payrollService.getMyPayroll(year, month);
    }

    // 특정 유저 급여 조회 (사장님용)
    @GetMapping("/store/{storeId}/user/{userId}")
    public MonthlyPayrollDto getUserPayroll(@PathVariable Long storeId, @PathVariable Long userId,
                                            @RequestParam int year, @RequestParam int month) {
        return payrollService.getUserPayroll(storeId, userId, year, month);
    }
    //시급 수정
    @PatchMapping("/wage")
    public ResponseEntity<Void> updateMyWage(@RequestBody WageUpdateRequest request) {
        payrollService.updateMyWage(request);
        return ResponseEntity.ok().build();
    }
}

