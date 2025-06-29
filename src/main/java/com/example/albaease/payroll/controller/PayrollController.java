package com.example.albaease.payroll.controller;

import com.example.albaease.payroll.dto.MonthlyPayrollDto;
import com.example.albaease.payroll.dto.WageUpdateRequest;
import com.example.albaease.payroll.service.PayrollService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payroll")
public class PayrollController {
    private final PayrollService payrollService;
    // 내 급여 조회
    @Operation(summary = "내 급여 조회", description = "현재 로그인한 알바생의 급여를 조회할 수 있습니다.")
    @GetMapping("/me")
    public MonthlyPayrollDto getMyPayroll(@RequestParam int year, @RequestParam int month) {
        return payrollService.getMyPayroll(year, month);
    }

    // 특정 유저 급여 조회 (사장님용)
    @Operation(summary = "알바생 급여 조회", description = "사장님이 알바생의 급여를 조회할 수 있습니다.")
    @GetMapping("/store/{storeId}/user/{userId}")
    public MonthlyPayrollDto getUserPayroll(@PathVariable Long storeId, @PathVariable Long userId,
                                            @RequestParam int year, @RequestParam int month) {
        return payrollService.getUserPayroll(storeId, userId, year, month);
    }
    //시급 수정
    @Operation(summary = "알바생 시급 수정", description = "알바생이 자신의 시급을 수정할 수 있습니다.")
    @PatchMapping("/wage")
    public ResponseEntity<Void> updateMyWage(@RequestBody WageUpdateRequest request) {
        payrollService.updateMyWage(request);
        return ResponseEntity.ok().build();
    }
}

