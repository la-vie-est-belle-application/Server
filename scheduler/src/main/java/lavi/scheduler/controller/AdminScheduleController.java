package lavi.scheduler.controller;

import lavi.scheduler.domain.ManagementStatus;
import lavi.scheduler.domain.Member;
import lavi.scheduler.domain.Position;
import lavi.scheduler.domain.Schedule;
import lavi.scheduler.service.AdminScheduleService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AdminScheduleController {
    //AdminScheduleController 는 전부 다 관리자만 사용할 수 있는 기능임. session 이용해서 구분하는 기능 추가하기
    private final AdminScheduleService adminScheduleService;

    // 서버 session rolltype 검증 로직 필요
//    public void serverSesisonCheck() {
//
//    }

    @PostMapping("/schedule/register")
    public ResponseEntity<Map<String, List<Schedule>>> registerSchedule(@RequestBody AddScheduleDto addScheduleDto) {

        log.info("[*]   입력 받은 날짜 이용해서 스케줄 등록");
        List<Schedule> scheduleList = adminScheduleService.registerSchedule(addScheduleDto.workingDate);

        if (scheduleList.isEmpty()) {
            log.info("[*]   스케줄 등록 실패");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("[*]   스케줄 등록 성공");
        Map<String, List<Schedule>> data = new HashMap<>();
        data.put("scheduleList", scheduleList);

        return ResponseEntity.ok().body(data);
    }

    // 날짜 상세보기 (출퇴근 시간, 명단, 포지션, 마감여부 상태 return)
    @GetMapping("/schedule/register/{workingDate}")
        public void detailSchedule(@PathVariable ScheduleDto workingDate) {

    }

    // 출퇴근시간 + 명단 + 포지션 + 마감여부 상태 업데이트
//    @PostMapping("/schedule/register/update")
//    public ResponseEntity<Map<String, Schedule>> updateSchedule(@RequestBody ScheduleDto scheduleDto, ScheduleManagementDto scheduleManagementDto) {
//
//        log.info("[*]   해당 날짜의 시간 정보 업데이트");
//        Schedule updatedSchedule = adminScheduleService
//                .updateSchedule(scheduleDto.getWorkingDate(), scheduleDto.getStartTime(), scheduleDto.getEndTime(),scheduleManagementDto.getMember(),scheduleManagementDto.getMembe(),scheduleManagementDto.getManagementStatus());
//
//        if (updatedSchedule == null) {
//            log.info("[*]   해당 날짜의 시간 정보 업데이트 실패");
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//        log.info("[*]   해당 날짜의 시간 정보 업데이트 성공");
//        Map<String, Schedule> data = new HashMap<>();
//        data.put("schedule", updatedSchedule);
//
//            return ResponseEntity.ok().body(data);
//    }

    // 해당 날짜 출근인원 조회

    // 매 월 출근인원 조회 (필요하면)

    // 포지션 수정 기능

    // 스케줄 삭제

    @Data
    static class AddScheduleDto {
        private List<LocalDate> workingDate;
    }

    @Data
    static class ScheduleDto{
        private LocalDate workingDate;
        private LocalTime startTime;
        private LocalTime endTime;
    }

    @Data
    static class ScheduleManagementDto {
        private Member member;
        private Position position;
        private ManagementStatus managementStatus;
    }


}
