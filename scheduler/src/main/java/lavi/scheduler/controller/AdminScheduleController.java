package lavi.scheduler.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lavi.scheduler.domain.*;
import lavi.scheduler.repository.MemberRepository;
import lavi.scheduler.service.AdminScheduleService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class AdminScheduleController {

    //AdminScheduleController 는 전부 다 관리자만 사용할 수 있는 기능임. session 이용해서 구분하는 기능 추가하기
    private final AdminScheduleService adminScheduleService;
    private final HttpServletRequest httpServletRequest;
    private final MemberRepository memberRepository;

    // 서버 session roletype 검증 로직 필요
    public void serverSessionCheck() throws Exception {

        //세션 생성
        HttpSession session = httpServletRequest.getSession();
//      String role = (String) session.getAttribute(String.valueOf(memberType.isRoleType()));
        Boolean isAdmin = (Boolean) session.getAttribute("roleType");
        if (isAdmin == null || !isAdmin) {
            throw new Exception("관리자 권한이 필요합니다");
        }

    }
    // 스케줄 일정 등록
    @PostMapping("/schedule/register")
    public ResponseEntity<Map<String, List<Schedule>>> registerSchedule(@RequestBody RegisterScheduleDto registerScheduleDto) throws Exception {
//        serverSessionCheck();
        log.info("[*]   입력 받은 날짜 이용해서 스케줄 등록");

        List<Schedule> scheduleList = adminScheduleService.registerSchedule(registerScheduleDto.getWorkingDate());
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
        public ResponseEntity<ScheduleDto> detailSchedule(@PathVariable String workingDate) throws Exception {
//        serverSessionCheck();
        LocalDate date = LocalDate.parse(workingDate);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    // 스케줄 업데이트
    @PostMapping("/schedule/register/update")
    public void updateSchedule(@RequestBody ScheduleUpdateDto scheduleUpdateDto) {

        adminScheduleService.updateSchedule(scheduleUpdateDto);
        //출, 퇴근 시간 들어오고 포지션이 들어 왔을 때 해당 날짜의 출, 퇴근 시간 및 해당 날짜 가지고 있는 schedule 정보에
        //해당 멤버의 포지션 및 상태 정보 수정

    }

    // 스케줄 마감
    @PostMapping("/")
    public void pixedSchedule() {

    }

    // 해당 날짜 출근인원 조회
    @GetMapping
    public void workingMember() {

    }

    // 매 월 출근인원 조회 (필요하면)

    // 포지션 수정 기능
    public void changePoistion() {

    }

    // 스케줄 삭제
    public void deleteSchedule() {

    }

    @Data
    class RegisterScheduleDto {
        private List<LocalDate> workingDate;
    }

    @Data
    class ScheduleDto {
        private LocalDate workingDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private Boolean scheduleStatus;
    }

    @Data
    public class MemberPositionDto {
        private Long id;
        private String name;
    }

    @Data
    public class ScheduleUpdateDto {
        private Schedule scheduleDto;
        private Map<String, List<MemberPositionDto>> positionDto;
    }
}
