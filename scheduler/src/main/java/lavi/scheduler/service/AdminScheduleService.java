package lavi.scheduler.service;

import jakarta.transaction.Transactional;
import lavi.scheduler.domain.ManagementStatus;
import lavi.scheduler.domain.Member;
import lavi.scheduler.domain.Position;
import lavi.scheduler.domain.Schedule;
import lavi.scheduler.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AdminScheduleService {

    private final ScheduleRepository scheduleRepository;

    public List<Schedule> registerSchedule(List<LocalDate> workingDateList) {

        //날짜 정보 들어오면 스케줄 리스트 생성
        List<Schedule> scheduleList = workingDateList.stream()
                .map(Schedule::new)
                .map(scheduleRepository::save)
                .toList();

        log.info("[*]    스케줄 리스트 DB 저장 완료 ={}", scheduleList);
        return scheduleList;
    }


//    public Schedule updateSchedule(LocalDate workingDate, LocalTime startTime, LocalTime endTime, Member member, Position position, ManagementStatus managementStatus) {
//        Schedule schedule = scheduleRepository.findByWorkingDate(workingDate);
//        if (schedule == null) {
//            //workingDate 로 찾은 schedule 이 존재하지 않는다면 return null
//            return null;
//        } else {
//            schedule.update(startTime, endTime, member, position, managementStatus);
//            log.info("[*]   스케줄 정보 업데이트 완료 출근 시간 ={}, 퇴근 시간 ={}", schedule.getStartTime(), schedule.getEndTime(), schedule.getMember(), schedule.getPosition(), schedule.getManagementStatus());
//            return schedule;
//        }
//    }


}
