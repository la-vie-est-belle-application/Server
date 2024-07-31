package lavi.scheduler.service;

import jakarta.transaction.Transactional;
import lavi.scheduler.domain.*;
import lavi.scheduler.repository.MemberRepository;
import lavi.scheduler.repository.ScheduleManagementRepository;
import lavi.scheduler.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AdminScheduleService {

    private final MemberRepository memberRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleManagementRepository scheduleManagementRepository;

    public List<Schedule> registerSchedule(List<LocalDate> workingDateList) {

        //날짜 정보 들어오면 스케줄 리스트 생성
        List<Schedule> scheduleList = workingDateList.stream()
                .map(Schedule::new)
                .map(scheduleRepository::save)
                .toList();

        log.info("[*]    스케줄 리스트 DB 저장 완료 ={}", scheduleList);
        return scheduleList;
    }


    // 날짜 조회해서 출퇴근시간 업데이트
    public Schedule updateTime(LocalDate workingDate, LocalTime startTime, LocalTime endTime) {
        Schedule schedule = scheduleRepository.findByWorkingDate(workingDate);
        if (schedule == null) {
            //workingDate 로 찾은 schedule 이 존재하지 않는다면 return null
            return null;
        } else {
            schedule.updateTime(startTime, endTime);

            log.info("[*]   스케줄 정보 업데이트 완료 출근 시간 ={}, 퇴근 시간 ={}", schedule.getStartTime(), schedule.getEndTime());
            return schedule;
        }
    }

    //  포지션, 출근여부 업데이트
    public List<ScheduleManagement> updatePositions(List<User> users, Position position, Schedule schedule) {
            List<ScheduleManagement> scheduleManagementsList = new ArrayList<>();
        for (User user : users) {
            ScheduleManagement scheduleManagement = scheduleManagementRepository.findByScheduleAndMemberId(schedule, user.getId());
            scheduleManagement.updateSchedule(position);
            scheduleManagementsList.add(scheduleManagement);
        }
        return scheduleManagementsList;
    }
}

