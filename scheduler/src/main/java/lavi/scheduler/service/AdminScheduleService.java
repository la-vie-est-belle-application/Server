package lavi.scheduler.service;

import jakarta.transaction.Transactional;
import lavi.scheduler.domain.*;
import lavi.scheduler.dto.MemberPositionDto;
import lavi.scheduler.dto.ScheduleDto;
import lavi.scheduler.dto.ScheduleUpdateDto;
import lavi.scheduler.repository.ScheduleManagementRepository;
import lavi.scheduler.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AdminScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final ScheduleManagementRepository scheduleManagementRepository;

    // 스케줄 일정 등록
    public List<Schedule> registerSchedule(List<LocalDate> workingDateList) {

        //날짜 정보 들어오면 스케줄 리스트 생성
        List<Schedule> scheduleList = workingDateList.stream()
                .map(Schedule::new)
                .toList();

        scheduleRepository.saveAll(scheduleList);

        log.info("[*]    스케줄 리스트 DB 저장 완료 ={}", scheduleList);
        return scheduleList;
    }

    // 날짜 상세보기 (출퇴근 시간, 명단, 포지션, 마감여부 상태 return)
    public Schedule detailSchedule(LocalDate date) {

        Schedule schedule = scheduleRepository.findByWorkingDate(date);
        if(schedule == null) {
            throw new IllegalArgumentException("존재하지 않는 스케줄 입니다." + date);
        }
        return schedule;
    }

    // 스케줄 업데이트
    public void updateSchedule(ScheduleUpdateDto scheduleUpdateDto) {

        Schedule scheduleDto = scheduleUpdateDto.getScheduleDto();

        Schedule schedule = scheduleRepository.findByWorkingDate(scheduleDto.getWorkingDate());
        schedule.updateTime(scheduleDto.getStartTime(), scheduleDto.getEndTime());

        Map<String, List<MemberPositionDto>> positionDto = scheduleUpdateDto.getPositionDto();

        positionDto.forEach((position, members) -> {
            for (MemberPositionDto member : members) {
                ScheduleManagement scheduleManagement = scheduleManagementRepository.findByScheduleIdAndMemberId(schedule.getId(), member.getId());
                scheduleManagement.updateSchedule(Position.valueOf(position.toUpperCase()));
            }
        });
    }

    // 스케줄 마감
    public Schedule fixedSchedule(ScheduleDto scheduleDto) {

        // 마감버튼 누르면 해당 월? 9/1 포함 되어있으면 우짬?? == 스케줄 하나하나 마감 시킬거라 월 로 받아도 가능할듯
        // 테스트 해보기

        LocalDate workingDate = scheduleDto.getWorkingDate();

        Schedule schedule = scheduleRepository.findByWorkingDate(workingDate);
        schedule.updateScheduleStatus(scheduleDto.getScheduleStatus());

//        boolean scheduleStatus = scheduleDto.getScheduleStatus();

        if(schedule.getScheduleStatus()) {
            throw new IllegalArgumentException("스케줄 마감 실패." + schedule);
        }
        return schedule;
    }

    // 해당 날짜 출근인원 조회
    public List<ScheduleManagement> workingMember(Long id) {

        List<ScheduleManagement> scheduleManagementList = scheduleManagementRepository.findAllByScheduleId(id);
        if (scheduleManagementList == null) {
            throw new IllegalArgumentException("존재하지 않는 스케줄 입니다." + id);
        }
        return scheduleManagementList;
    }

    // 매 월 출근인원 조회 (필요하면)

    // 포지션 수정 기능
    public void changePosition() {

    }

    // 스케줄 삭제
    public void deleteSchedule() {

    }
}


