package lavi.scheduler.service;

import lavi.scheduler.domain.*;
import lavi.scheduler.repository.MemberRepository;
import lavi.scheduler.repository.ScheduleManagementRepository;
import lavi.scheduler.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserScheduleService {

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

    public boolean registerSchedule(Long id, List<LocalDate> registerDateList) {
        //해당 id에 맞는 회원 찾기
        Member member = getMemberById(id);

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

    //해당 달에 출근하는 날짜 정보 넘기기
    public List<LocalDate> getWorkingDayList(Long memberId, LocalDate workingDate) {
        int month = workingDate.getMonthValue();
        int year = workingDate.getYear();
        return scheduleManagementRepository.findByMemberIdAndMonth(memberId, year, month);
    }

    //회원 id와 날짜가 넘어오면 해당 회원이 그 날짜에 해당하는 position 을 알려준다.
    public Position getPositionInfo(Long memberId, LocalDate workingDate) {

        log.info("[*]   해당 날짜 나의 포지션 확인 = {}", workingDate);
        ScheduleManagement scheduleManagement = scheduleManagementRepository.findByMemberIdAndSchedule_WorkingDate(memberId, workingDate);
        if (scheduleManagement == null) {
            new IllegalArgumentException("등록하지 않은 회원 또는 스케줄 입니다.");
        }
        //승인상태일 때만 position 정보를 들고 있다. 그래서 그냥 return 후 controller 에서 처리
        return scheduleManagement.getPosition();
    }

    //registerDateList 받아와서 기존에 있던 registerDate 과 비교 작업 수행
    public void editRegisterDate(Long memberId, List<LocalDate> registerDateList) {

        Member member = getMemberById(memberId);

        //기존에 member 가 갖고 있던 schedule 정보 가져오기
        List<ScheduleManagement> scheduleManagements = scheduleManagementRepository.findAllByMemberId(memberId);

        Map<LocalDate, ScheduleManagement> existScheduleMap = scheduleManagements.stream()
                .collect(Collectors.toMap(
                        sm -> sm.getSchedule().getWorkingDate(),
                        Function.identity()
                ));

        Set<LocalDate> existScheduleSet = existScheduleMap.keySet();

        //existScheduleList 있고, registerDateList 없으면 삭제
        Set<LocalDate> datesToDelete = existScheduleSet.stream()
                .filter(date -> !registerDateList.contains(date))
                .collect(Collectors.toSet());

        //registerDateList 있고, existScheduleList 없으면 추가
        Set<LocalDate> datesToAdd = registerDateList.stream()
                .filter(date -> !existScheduleSet.contains(date))
                .collect(Collectors.toSet());

        if (!datesToAdd.isEmpty()) {
            List<Schedule> scheduleToAdd = scheduleRepository.findAllByWorkingDateIn(datesToAdd);
            List<ScheduleManagement> scheduleManagementToAdd = scheduleToAdd.stream()
                    .map(schedule -> new ScheduleManagement(schedule, member))
                    .toList();
            scheduleManagementRepository.saveAll(scheduleManagementToAdd);
        }

        if (!datesToDelete.isEmpty()) {
            List<ScheduleManagement> scheduleManagementToDelete = datesToDelete.stream()
                    .map(existScheduleMap::get)
                    .toList();
            scheduleManagementRepository.deleteAll(scheduleManagementToDelete);
        }
    }

    private Member getMemberById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("유효하지 않은 회원 번호 입니다 : " + id)
        );
        return member;
    }



}
