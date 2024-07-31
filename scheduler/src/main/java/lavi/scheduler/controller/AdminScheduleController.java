package lavi.scheduler.controller;

import lavi.scheduler.domain.*;
import lavi.scheduler.service.AdminScheduleService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

    // 출퇴근시간 + 포지션 + 출근여부 상태 업데이트
    @PostMapping("/schedule/register/update")
    public ResponseEntity<String> updateSchedule(@RequestBody ScheduleDto scheduleDto, PositionDto positionDto) {

        log.info("[*]   출퇴근 시간 업데이트 시작");
        Schedule updatedSchedule = adminScheduleService
                .updateTime(scheduleDto.getWorkingDate(), scheduleDto.getStartTime(), scheduleDto.getEndTime());

        if (updatedSchedule == null) {
            log.info("[*]   해당 날짜 출퇴근 시간 업데이트 실패");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
            log.info("[*]   해당 날짜 출퇴근 시간 업데이트 성공");
            Map<String, Schedule> data = new HashMap<>();
            data.put("schedule", updatedSchedule);

        if (updatedSchedule.getWorkingDate() != null ){
            log.info("[*]   포지션 정보 업데이트 시작");
            List<User> leader = positionDto.getLeader();
            List<User> scanner = positionDto.getScan();
            List<User> main = positionDto.getMain();
            List<User> dress = positionDto.getDress();
            List<User> assistant = positionDto.getAssistant();
            List<User> waitingRoom = positionDto.getWaitingRoom();
            List<User> manager = positionDto.getManager();
            List<User> navigator = positionDto.getNavigator();
            List<User> dressRoom = positionDto.getDressRoom();

            updatePosition(leader, Position.LEADER, updatedSchedule);
            updatePosition(scanner, Position.SCAN, updatedSchedule);
            updatePosition(main, Position.MAIN, updatedSchedule);
            updatePosition(dress, Position.DRESS, updatedSchedule);
            updatePosition(assistant, Position.ASSISTANT, updatedSchedule);
            updatePosition(waitingRoom, Position.WAITINGROOM, updatedSchedule);
            updatePosition(manager, Position.MANAGER, updatedSchedule);
            updatePosition(navigator, Position.NAVIGATOR, updatedSchedule);
            updatePosition(dressRoom, Position.DRESSROOM, updatedSchedule);
        }
            log.info("[*]   해당 날짜 포지션 업데이트 성공");
            data.put("schedule", updatedSchedule);
            return ResponseEntity.ok().build();

    }

    private List<ScheduleManagement> updatePosition(List<User> userList, Position position, Schedule updatedSchedule) {
        List<ScheduleManagement> scheduleManagementList = adminScheduleService.updatePositions(userList, position, updatedSchedule);
        return scheduleManagementList;
    }

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


}
