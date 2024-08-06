package lavi.scheduler.service;

import jakarta.transaction.Transactional;
import lavi.scheduler.controller.AdminScheduleController;
import lavi.scheduler.domain.*;
import lavi.scheduler.repository.MemberRepository;
import lavi.scheduler.repository.ScheduleManagementRepository;
import lavi.scheduler.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AdminScheduleService {

    private final MemberRepository memberRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleManagementRepository scheduleManagementRepository;

    // 스케줄 일정 등록
    public List<Schedule> registerSchedule(List<LocalDate> workingDateList) {

        //날짜 정보 들어오면 스케줄 리스트 생성
        List<Schedule> scheduleList = workingDateList.stream()
                .map(Schedule::new)
                .map(scheduleRepository::save)
                .toList();

        log.info("[*]    스케줄 리스트 DB 저장 완료 ={}", scheduleList);
        return scheduleList;
    }

    // 날짜 상세보기 (출퇴근 시간, 명단, 포지션, 마감여부 상태 return)
    public Schedule detailSchedule(LocalDate date) {
        return scheduleRepository.findByWorkingDate(date);
    }

    // 스케줄 업데이트
    public void updateSchedule(AdminScheduleController.ScheduleUpdateDto scheduleUpdateDto) {

        Schedule scheduleDto = scheduleUpdateDto.getScheduleDto();

        Schedule schedule = scheduleRepository.findByWorkingDate(scheduleDto.getWorkingDate());
        schedule.updateTime(scheduleDto.getStartTime(), scheduleDto.getEndTime());

        Map<String, List<AdminScheduleController.MemberPositionDto>> positionDto = scheduleUpdateDto.getPositionDto();

        positionDto.forEach((position, members) -> {
            for (AdminScheduleController.MemberPositionDto member : members) {
                ScheduleManagement scheduleManagement = scheduleManagementRepository.findByScheduleIdAndMemberId(schedule.getId(), member.getId());
                scheduleManagement.updateSchedule(Position.valueOf(position.toUpperCase()));
            }
        });
    }

    // 스케줄 마감
    public void pixedSchedule() {

    }

    // 해당 날짜 출근인원 조회
    public void workinMember() {

    }

    // 매 월 출근인원 조회 (필요하면)

    // 포지션 수정 기능
    public void changePosition() {

    }

    // 스케줄 삭제
    public void deleteSchedule() {

    }
}

