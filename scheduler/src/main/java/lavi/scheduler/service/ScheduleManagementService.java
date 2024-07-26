package lavi.scheduler.service;

import lavi.scheduler.domain.Member;
import lavi.scheduler.domain.Schedule;
import lavi.scheduler.domain.ScheduleManagement;
import lavi.scheduler.repository.MemberRepository;
import lavi.scheduler.repository.ScheduleManagementRepository;
import lavi.scheduler.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ScheduleManagementService {

    private final MemberRepository memberRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleManagementRepository scheduleManagementRepository;

    //스케줄에 등록된 날짜 맞는지 검증
    public List<LocalDate> verifyDate(List<LocalDate> registerDateList) {
        log.info("[*]   출근 가능 날짜 검증");

        List<LocalDate> unverifiedDate = registerDateList.stream()
                .filter(date -> !scheduleRepository.existsByWorkingDate(date))
                .toList();

        log.info("[*]   스케줄에 등록되지 않은 날짜 ={}", unverifiedDate);
        return unverifiedDate;
    }

    public boolean addScheduleManagement(Long id, List<LocalDate> registerDateList) {
        //해당 id에 맞는 회원 찾기
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("유효하지 않은 회원 번호 입니다 : " + id)
        );

        //해당 날짜와 일치하는 스케줄 번호 불러오기
        for (LocalDate date : registerDateList) {
            Schedule schedule = scheduleRepository.findByWorkingDate(date);
            if (schedule == null) {
                new IllegalArgumentException("존재하지 않는 날짜 입니다." + date);
                return false;
            }
            ScheduleManagement scheduleManagement = new ScheduleManagement(schedule, member);
            scheduleManagementRepository.save(scheduleManagement);
        }
        return true;
    }
}
