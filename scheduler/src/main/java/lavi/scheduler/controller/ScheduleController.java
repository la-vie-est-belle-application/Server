package lavi.scheduler.controller;

import lavi.scheduler.domain.ResponseDto;
import lavi.scheduler.domain.Schedule;
import lavi.scheduler.service.ScheduleService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ScheduleController {
    //ScheduleController 는 전부 다 관리자만 사용할 수 있는 기능임. session 이용해서 구분하는 기능 추가하기
    private final ScheduleService scheduleService;

    @PostMapping("/schedule/add")
    public ResponseEntity<ResponseDto> addSchedule(@RequestBody AddScheduleDto addScheduleDto) {
        log.info("[*]   입력 받은 날짜 이용해서 스케줄 등록");
        List<Schedule> scheduleList = scheduleService.addSchedule(addScheduleDto.workingDate);

        if (scheduleList.isEmpty()) {
            log.info("[*]   스케줄 등록 실패");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto("스케줄 등록 실패", false));
        }
        log.info("[*]   스케줄 등록 성공");
        Map<String, List<Schedule>> data = new HashMap<>();
        data.put("scheduleList", scheduleList);

        return ResponseEntity.ok()
                .body(new ResponseDto("스케줄 등록 성공", true, data));
    }

    @PostMapping("/schedule/update")
    public ResponseEntity<ResponseDto> updateSchedule(@RequestBody ScheduleDto scheduleDto) {
        log.info("[*]   해당 날짜의 시간 정보 업데이트");
        Schedule schedule = scheduleService.updateSchedule(scheduleDto.workingDate, scheduleDto.startTime, scheduleDto.endTime);
        if (schedule == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("등록되지 않은 날짜입니다.", false));
        }
        return ResponseEntity.ok()
                .body(new ResponseDto("시간 정보 업데이트 완료", true));
    }

    @Data
    static class AddScheduleDto {
        private List<LocalDate> workingDate;
    }

    @Data
    static class ScheduleDto{
        private LocalDate workingDate;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
    }


}
