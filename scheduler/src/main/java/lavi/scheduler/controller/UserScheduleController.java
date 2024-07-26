package lavi.scheduler.controller;

import lavi.scheduler.domain.ResponseDto;
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
    public ResponseEntity<ResponseDto> registerDate(@RequestBody RegisterDto registerDto) {

        //출근 가능 날짜 리스트 중에 스케줄에 등록된 날짜가 맞는지 검증하기
        List<LocalDate> unverifiedDate = userScheduleService.verifyDate(registerDto.registerDateList);
        if (unverifiedDate.isEmpty()) {
            //다 등록된 날짜인 경우 member, schedule, 거절 넣어서 scheduleManagement 객체 생성
            boolean result = userScheduleService.registerSchedule(registerDto.id, registerDto.registerDateList);
            if (result) {
                log.info("[*]   스케줄 신청 성공");
                return ResponseEntity.ok()
                        .body(new ResponseDto<>("스케줄 신청 성공", true));
            }
            log.info("[*]   스케줄 신청 실패");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDto<>("날짜 또는 회원 정보가 일치하지 않습니다.", false));
        }
        Map<String, List<LocalDate>> data = new HashMap<>();
        data.put("unverifiedDate", unverifiedDate);

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDto<>("등록된 날짜가 아닙니다.", false, data));
    }

    // 한 달 출근 날짜 조회

    // 출근 가능 날짜 삭제 및 추가


    @Data
    static class RegisterDto {
        private Long id;    //회원 번호
        private List<LocalDate> registerDateList;   //출근 가능 날짜 리스트
    }

    @Data
    static class WorkingDateDto {
        private LocalDate workingDate;
    }


}
