package lavi.scheduler.controller;

import lavi.scheduler.domain.Position;
import lavi.scheduler.service.UserScheduleService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserScheduleController {

    private final UserScheduleService userScheduleService;

    // 출근 가능 날짜 입력
    @PostMapping("/register")
    public ResponseEntity<Map<String, List<LocalDate>>> registerDate(@RequestBody RegisterDto registerDto) {

        //출근 가능 날짜 리스트 중에 스케줄에 등록된 날짜가 맞는지 검증하기
        List<LocalDate> unverifiedDate = userScheduleService.verifyDate(registerDto.registerDateList);
        if (unverifiedDate.isEmpty()) {
            //다 등록된 날짜인 경우 member, schedule, 거절 넣어서 scheduleManagement 객체 생성
            boolean result = userScheduleService.registerSchedule(registerDto.id, registerDto.registerDateList);
            if (result) {
                log.info("[*]   스케줄 신청 성공");
                return ResponseEntity.ok().build();
            }
            log.info("[*]   스케줄 신청 실패");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        Map<String, List<LocalDate>> data = new HashMap<>();
        data.put("unverifiedDate", unverifiedDate);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(data);
    }

    //한 달 출근 날짜 조회 (accept 상태, wait 상태 상관 없이 다 조회) - 분리 필요 할 듯 .. >
    @PostMapping("/mySchedule")
    public Map<String, List<LocalDate>> getWorkingDayList(@RequestBody RegisterDto registerDto) {

        //달 정보가 아예 들어오면 베스트
        log.info("[*]   한 달 출근 날짜 조회 = {}, {}", registerDto.workingDate, registerDto.id);
        List<LocalDate> monthWorkingDayList = userScheduleService.getWorkingDayList(registerDto.id, registerDto.workingDate);

        Map<String, List<LocalDate>> data = new HashMap<>();
        data.put("workingDateList", monthWorkingDayList);

        return data;
    }

     //출근 가능 날짜 삭제 및 추가 (수정하기 전에 마감상태인지 아닌지 확인하는 과정 필요)
    @PostMapping("/register/edit")
    public ResponseEntity<Map<String, List<LocalDate>>> editRegisterDate(@RequestBody RegisterDto registerDto) {

        log.info("[*]   출근 가능 날짜 수정");
        List<LocalDate> unverifiedDate = userScheduleService.verifyDate(registerDto.registerDateList);
        if (unverifiedDate.isEmpty()) {
            userScheduleService.editRegisterDate(registerDto.id, registerDto.registerDateList);
            log.info("[*]   수정된 날짜 확인");
            List<LocalDate> editWorkingDayList = userScheduleService.getWorkingDayList(registerDto.id, registerDto.registerDateList.get(0));

            Map<String, List<LocalDate>> data = new HashMap<>();
            data.put("workingDateList", editWorkingDayList);

            return ResponseEntity.ok(data);
        }
        return ResponseEntity.notFound().build();
    }

    // 자기 배정받은 포지션 확인하기
    @PostMapping("/myPosition")
    public ResponseEntity<Map<String,Object>> getMyPosition(@RequestBody RegisterDto registerDto) {

        log.info("[*]   출근 날짜 = {} 의 배정 받은 포지션 확인하기", registerDto.workingDate);
        Position position = userScheduleService.getPositionInfo(registerDto.id, registerDto.workingDate);

        if (position == null) {
            //거절 혹은 대기 상태
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Map<String, Object> data = new HashMap<>();
        data.put("workingDate", registerDto.workingDate);
        data.put("position", position);

        return ResponseEntity.ok().body(data);
    }

    @Data
    static class RegisterDto {
        private Long id;    //회원 번호
        private List<LocalDate> registerDateList;   //출근 가능 날짜 리스트
        private LocalDate workingDate;
    }

}

