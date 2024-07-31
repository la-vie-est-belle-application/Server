package lavi.scheduler.repository;

import lavi.scheduler.domain.Member;
import lavi.scheduler.domain.Schedule;
import lavi.scheduler.domain.ScheduleManagement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleManagementRepository extends JpaRepository<ScheduleManagement, Long> {

    ScheduleManagement findByScheduleAndMemberId(Schedule schedule, Long id);

}


