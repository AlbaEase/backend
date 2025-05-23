package com.example.albaease.schedule.service;

import com.example.albaease.auth.CustomUserDetails;
import com.example.albaease.schedule.domain.Schedule;
import com.example.albaease.schedule.domain.Template;
import com.example.albaease.schedule.dto.ScheduleRequest;
import com.example.albaease.schedule.dto.ScheduleResponse;
import com.example.albaease.schedule.dto.TemplateScheduleRequest;
import com.example.albaease.schedule.repository.ScheduleRepository;
import com.example.albaease.shift.domain.entity.Shift;
import com.example.albaease.shift.domain.enums.ShiftStatus;
import com.example.albaease.shift.repository.ShiftRepository;
import com.example.albaease.store.domain.Store;
import com.example.albaease.store.repository.StoreRepository;
import com.example.albaease.user.entity.User;
import com.example.albaease.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final TemplateService templateService;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final ShiftRepository shiftRepository;

    // 특정 스토어(storeId)의 전체 스케줄 조회
    public List<ScheduleResponse> getSchedulesByStoreId(Long storeId) {
        List<Schedule> schedules = scheduleRepository.findByStore_Id(storeId);

        return schedules.stream()
                .map(ScheduleResponse::fromEntity)
                .collect(Collectors.toList());
    }

    //특정 스토어 내 특정 사용자의 스케줄 조회
    public List<ScheduleResponse> getSchedulesByStoreAndUser(Long storeId, Long userId) {
        List<Schedule> schedules = scheduleRepository.findByStore_IdAndUser_UserId(storeId, userId);
        return schedules.stream()
                .map(ScheduleResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정 스케줄 ID로 조회
    public ScheduleResponse getScheduleById(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found with id: " + scheduleId));
        return ScheduleResponse.fromEntity(schedule);
    }

    // 스케줄 생성
    @Transactional
    public List<ScheduleResponse> createSchedules(Long storeId, ScheduleRequest request) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + storeId));

        List<User> users = userRepository.findAllById(request.getUserIds());
        if (users.isEmpty()) {
            throw new RuntimeException("No valid users found.");
        }

        List<Schedule> schedules = new ArrayList<>();
        for (User user : users) {
            Schedule schedule = new Schedule();
            schedule.setStore(store);
            schedule.setUser(user);
            schedule.setWorkDate(request.getWorkDate());
            schedule.setStartTime(request.getStartTime());
            schedule.setEndTime(request.getEndTime());
            schedule.setBreakTime(request.getBreakTime());
            schedule.setRepeatDaysFromList(request.getRepeatDays());
            schedule.setRepeatEndDate(request.getRepeatEndDate());
            schedules.add(schedule);
        }

        // 스케줄 저장
        scheduleRepository.saveAll(schedules);

        // 저장된 데이터를 ScheduleResponse로 변환 후 반환
        return schedules.stream()
                .map(ScheduleResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 특정 스케줄 삭제 (스토어 ID, 사용자 ID 확인 후 삭제)
    public void deleteSchedule(Long storeId, Long userId, Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (!schedule.getStore().getId().equals(storeId) ||
                !schedule.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Store ID or User ID mismatch");
        }

        scheduleRepository.delete(schedule);
    }

    // 템플릿을 기반으로 스케줄 생성
    @Transactional
    public void createScheduleFromTemplate(Long templateId, TemplateScheduleRequest scheduleRequest) {
        // Template 조회
        Template template = templateService.getTemplateById(templateId);
        if (template == null) {
            throw new RuntimeException("Template not found with id: " + templateId);
        }

        Long storeId = scheduleRequest.getStoreId();
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found with ID: " + storeId));

        // 지정된 사용자들로 스케줄 생성
        List<Schedule> schedules = scheduleRequest.getUserIds().stream()
                .map(userId -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

                    Schedule schedule = new Schedule();
                    schedule.setUser(user);
                    schedule.setStore(store); // storeId 대신 store 객체 설정
                    schedule.setWorkDate(scheduleRequest.getWorkDate());
                    schedule.setStartTime(template.getStartTime());
                    schedule.setEndTime(template.getEndTime());
                    schedule.setBreakTime(template.getBreakTime());
                    schedule.setRepeatDaysFromList(scheduleRequest.getRepeatDays());
                    schedule.setRepeatEndDate(scheduleRequest.getRepeatEndDate());

                    return schedule;
                })
                .collect(Collectors.toList());

        scheduleRepository.saveAll(schedules);
    }




    // 스케줄 수정
    @Transactional
    public ScheduleResponse updateSchedule(Long storeId, Long userId, Long scheduleId, ScheduleRequest scheduleRequest, CustomUserDetails userDetails) {
        // 스케줄 조회
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        // 권한 확인 - OWNER (관리자) 권한 확인
        if (!userDetails.getRole().equals("OWNER")) {
            throw new RuntimeException("403 Forbidden: Only OWNER can update schedules.");
        }

        // 기존 스케줄 정보 변경 (userIds와 storeId는 무시)
        schedule.setWorkDate(scheduleRequest.getWorkDate());
        schedule.setStartTime(scheduleRequest.getStartTime());
        schedule.setEndTime(scheduleRequest.getEndTime());
        schedule.setBreakTime(scheduleRequest.getBreakTime());
        schedule.setRepeatDaysFromList(scheduleRequest.getRepeatDays());
        schedule.setRepeatEndDate(scheduleRequest.getRepeatEndDate());

        // 변경된 스케줄 저장
        Schedule updatedSchedule = scheduleRepository.save(schedule);

        // 수정된 스케줄 반환
        return ScheduleResponse.fromEntity(updatedSchedule);
    }




    @Transactional(readOnly = true)
    public List<ScheduleResponse> getSchedulesByUserId(Long userId) {
        // userId를 이용해 User 객체 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        List<Schedule> schedules = scheduleRepository.findByUser(user);

        return schedules.stream()
                .map(ScheduleResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // 월간 스케줄 조회 메서드 추가 (대타 요청 반영)
    @Transactional(readOnly = true)
    public Map<String, List<ScheduleResponse>> getMonthlyScheduleWithShifts(Long storeId, int year, int month) {
        // 1. 해당 월의 시작일과 종료일 계산
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        // 2. 해당 스토어의 기본 스케줄 조회
        List<Schedule> schedules = scheduleRepository.findByStore_Id(storeId);

        // 3. 승인된 대타 요청 조회
        List<Shift> approvedShifts = shiftRepository.findApprovedShiftsForMonth(
                ShiftStatus.APPROVED, year, month);

        // 4. 날짜별로 스케줄 확장 및 대타 요청 적용
        return generateMonthlySchedule(schedules, approvedShifts, startDate, endDate);
    }

    // 날짜별 스케줄 생성 및 대타 요청 적용 메서드
    private Map<String, List<ScheduleResponse>> generateMonthlySchedule(
            List<Schedule> schedules, List<Shift> approvedShifts,
            LocalDate startDate, LocalDate endDate) {

        Map<String, List<ScheduleResponse>> result = new HashMap<>();

        // 1. 날짜별로 스케줄 생성 (반복 패턴 확장)
        for (Schedule schedule : schedules) {
            // 단일 날짜 스케줄인 경우
            if (schedule.getWorkDate() != null) {
                if (!schedule.getWorkDate().isBefore(startDate) && !schedule.getWorkDate().isAfter(endDate)) {
                    addScheduleToResult(result, schedule, schedule.getWorkDate());
                }
            }
            // 반복 패턴이 있는 경우
            else if (schedule.getRepeatDays() != null && !schedule.getRepeatDays().isEmpty()) {
                List<String> repeatDays = schedule.getRepeatDaysList();
                LocalDate currentDate = startDate;

                while (!currentDate.isAfter(endDate)) {
                    // 해당 날짜의 요일 확인 (1:월, 2:화, ..., 7:일)
                    int dayOfWeek = currentDate.getDayOfWeek().getValue();
                    String dayOfWeekStr = String.valueOf(dayOfWeek);

                    // 반복 요일에 포함된 경우 스케줄 추가
                    if (repeatDays.contains(dayOfWeekStr)) {
                        addScheduleToResult(result, schedule, currentDate);
                    }

                    currentDate = currentDate.plusDays(1);
                }
            }
        }

        // 2. 승인된 대타 요청 적용
        for (Shift shift : approvedShifts) {
            if (shift.getStatus() == ShiftStatus.APPROVED && shift.getRequestDate() != null) {
                // 해당 날짜의 스케줄 조회
                String dateKey = shift.getRequestDate().toString();
                if (result.containsKey(dateKey)) {
                    List<ScheduleResponse> daySchedules = result.get(dateKey);

                    // 해당 날짜의 스케줄 중 대타 요청과 관련된 스케줄 찾기
                    for (int i = 0; i < daySchedules.size(); i++) {
                        ScheduleResponse scheduleResponse = daySchedules.get(i);

                        // 스케줄 ID와 사용자 ID가 일치하는 스케줄 찾기 (from_user가 일치해야 함)
                        if (scheduleResponse.getScheduleId().equals(shift.getSchedule().getScheduleId()) &&
                                scheduleResponse.getUserId().equals(shift.getFromUser().getUserId())) {

                            // 대타 정보로 수정된 스케줄 생성
                            ScheduleResponse updatedSchedule = new ScheduleResponse();
                            updatedSchedule.setScheduleId(scheduleResponse.getScheduleId());
                            updatedSchedule.setUserId(shift.getToUser().getUserId());
                            updatedSchedule.setFullName(shift.getToUser().getLastName() + shift.getToUser().getFirstName());
                            updatedSchedule.setStoreId(scheduleResponse.getStoreId());
                            updatedSchedule.setWorkDate(scheduleResponse.getWorkDate());
                            updatedSchedule.setStartTime(scheduleResponse.getStartTime());
                            updatedSchedule.setEndTime(scheduleResponse.getEndTime());
                            updatedSchedule.setBreakTime(scheduleResponse.getBreakTime());

                            // 대타 변경 여부 표시
                            updatedSchedule.setShiftChanged(true);
                            updatedSchedule.setOriginalUserId(shift.getFromUser().getUserId());
                            updatedSchedule.setOriginalUserName(shift.getFromUser().getLastName() + shift.getFromUser().getFirstName());

                            // 기존 스케줄 대체
                            daySchedules.set(i, updatedSchedule);
                            break;
                        }
                    }
                }
            }
        }

        return result;
    }

    // 결과 맵에 스케줄 추가 헬퍼 메서드
    private void addScheduleToResult(Map<String, List<ScheduleResponse>> result, Schedule schedule, LocalDate date) {
        String dateKey = date.toString();

        // 날짜별 스케줄 리스트 가져오기
        List<ScheduleResponse> daySchedules = result.getOrDefault(dateKey, new ArrayList<>());

        // 스케줄 응답 생성
        ScheduleResponse response = ScheduleResponse.fromEntity(schedule);
        response.setWorkDate(date); // 실제 근무일 설정

        // 결과에 추가
        daySchedules.add(response);
        result.put(dateKey, daySchedules);
    }
}
