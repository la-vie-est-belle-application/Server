package lavi.scheduler.repository;

import lavi.scheduler.domain.ScheduleManagement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface ScheduleManagementRepository extends JpaRepository<ScheduleManagement, Long> {

    ScheduleManagement findByScheduleIdAndMemberId(Long schedule, Long id);

    ScheduleManagement findByMemberIdAndSchedule_WorkingDate(Long memberId, LocalDate workingDate);

    @Query("" +
            "select sm.schedule.workingDate " +
            "from ScheduleManagement sm " +
            "where sm.member.id = :memberId " +
            "AND function('YEAR', sm.schedule.workingDate) = :year " +
            "AND function('MONTH', sm.schedule.workingDate) = :month ")
    List<LocalDate> findByMemberIdAndMonth(@Param("memberId") Long memberId, @Param("year") int year, @Param("month") int month);

    List<ScheduleManagement> findAllByMemberId(Long memberId);

    List<ScheduleManagement> findAllByScheduleId(@Param("scheduleId") Long scheduleId);

}


