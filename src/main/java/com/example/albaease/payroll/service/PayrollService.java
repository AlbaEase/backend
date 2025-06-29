package com.example.albaease.payroll.service;

import com.example.albaease.auth.CustomUserDetails;
import com.example.albaease.payroll.dto.DailyWageDto;
import com.example.albaease.payroll.dto.MonthlyPayrollDto;
import com.example.albaease.payroll.dto.WageUpdateRequest;
import com.example.albaease.schedule.domain.Schedule;
import com.example.albaease.schedule.repository.ScheduleRepository;
import com.example.albaease.store.domain.UserStoreRelationship;
import com.example.albaease.store.repository.UserStoreRelationshipRepository;
import com.example.albaease.user.entity.Role;
import com.example.albaease.user.entity.User;
import com.example.albaease.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PayrollService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final UserStoreRelationshipRepository relationshipRepository;

    // 현재 로그인 유저의 급여 조회 (내 급여)
    public MonthlyPayrollDto getMyPayroll(int year, int month) {
        Long userId = getCurrentUserId();
        return calculatePayroll(null, userId, year, month);
    }
    // 특정 알바생의 급여 조회 (사장용)
    public MonthlyPayrollDto getUserPayroll(Long storeId, Long userId, int year, int month) {
        return calculatePayroll(storeId, userId, year, month);
    }

    // 실제 급여 계산
    private MonthlyPayrollDto calculatePayroll(Long storeId, Long userId, int year, int month) {
        // 유저 조회
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저 없음"));

        // 알바생일때만 급여 계산
        if (user.getRole() != Role.WORKER) {
            return new MonthlyPayrollDto(0, List.of()); // 알바가 아니면 급여 없음
        }

        // 해당 월 시작~끝 날짜 구하기
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        // storeId 유무에 따라 스케줄 조회
        List<Schedule> schedules = (storeId == null)
                ? scheduleRepository.findByUser_UserIdAndWorkDateBetween(userId, start, end)
                : scheduleRepository.findByStore_IdAndUser_UserIdAndWorkDateBetween(storeId, userId, start, end);

        // 스케줄 기반으로 급여 계산
        List<DailyWageDto> dailyWageList = schedules.stream()
                .map(s -> {
                    UserStoreRelationship rel = relationshipRepository
                            .findByUser_UserIdAndStore_Id(userId, s.getStore().getId())
                            .orElse(null);

                    // 💡 시급 결정: 관계에 시급이 있으면 그것을, 없으면 store의 기본 시급 사용
                    Integer hourlyWage = (rel != null && rel.getHourlyWage() != null)
                            ? rel.getHourlyWage()
                            : s.getStore().getDefaultHourlyWage(); // 👈 여기가 추가된 부분

                    if (hourlyWage == null) return null;

                    // 근무 시간 계산
                    double hours = calculateWorkingHours(s);
                    int wage = (int) (hours * hourlyWage);
                    return new DailyWageDto(s.getWorkDate(), hours, wage);
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
        int totalWage = dailyWageList.stream()
                .mapToInt(DailyWageDto::getDailyWage)
                .sum();
        return new MonthlyPayrollDto(totalWage, dailyWageList);
    }
    // 사용자 ID 추출
    private Long getCurrentUserId() {
        CustomUserDetails userDetails = (CustomUserDetails)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUserId();
    }

    private double calculateWorkingHours(Schedule schedule) {
        // 근무 전체 시간 계산
        Duration total = Duration.between(schedule.getStartTime(), schedule.getEndTime());

        // breakTime(LocalTime) → 분 단위로 변환
        long breakMinutes = schedule.getBreakTime() != null ? schedule.getBreakTime().toSecondOfDay() / 60 : 0;

        // 실근무 시간 = 전체 시간 - 휴게시간
        long workingMinutes = total.toMinutes() - breakMinutes;

        return workingMinutes / 60.0;
    }
    public void updateMyWage(WageUpdateRequest request) {
        Long userId = getCurrentUserId();

        UserStoreRelationship rel = relationshipRepository
                .findByUser_UserIdAndStore_Id(userId, request.getStoreId())
                .orElseThrow(() -> new RuntimeException("매장과의 관계 없음"));

        rel.setHourlyWage(request.getHourlyWage()); // 개인 시급 저장
        relationshipRepository.save(rel);
    }
}
