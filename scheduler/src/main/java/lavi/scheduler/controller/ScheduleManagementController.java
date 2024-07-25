package lavi.scheduler.controller;

import lavi.scheduler.domain.ResponseDto;
import lavi.scheduler.service.ScheduleManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ScheduleManagementController {

    private final ScheduleManagementService scheduleManagementService;

    //1. 신청 정보 받기
    @GetMapping("/register")
    public ResponseEntity<ResponseDto> registerDate(@RequestBody RegisterDto registerDto) {

        //출근 가능 날짜 리스트 중에 스케줄에 등록된 날짜가 맞는지 검증하기
        List<LocalDate> unverifiedDate = scheduleManagementService.verifyDate(registerDto.registerDateList);
        if (unverifiedDate.isEmpty()) {
            //다 등록된 날짜인 경우 member, schedule, 거절 넣어서 scheduleManagement 객체 생성
            boolean result = scheduleManagementService.addScheduleManagement(registerDto.id, registerDto.registerDateList);
            if (result) {
                log.info("[*]   스케줄 신청 성공");
                return ResponseEntity.ok()
                        .body(new ResponseDto("스케줄 신청 성공", true));
            }
            log.info("[*]   스케줄 신청 실패");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto("날짜 또는 회원 정보가 일치하지 않습니다.", false));
        }
        Map<String, List<LocalDate>> data = new HashMap<>();
        data.put("unverifiedDate", unverifiedDate);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto("등록된 날짜가 아닙니다.", false, data));
    }

    //2. 해당 날짜에 출근 가능한 사람 정보 넘기기

    //3. 배정 받은 포지션, 사람 받아오기

    static class RegisterDto {
        private Long id;    //회원 번호
        private List<LocalDate> registerDateList;   //출근 가능 날짜 리스트
    }

}
