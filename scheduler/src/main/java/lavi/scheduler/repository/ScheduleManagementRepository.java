package lavi.scheduler.repository;

import lavi.scheduler.domain.Schedule;
import lavi.scheduler.domain.ScheduleManagement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleManagementRepository extends JpaRepository<ScheduleManagement, Long> {

      ScheduleManagement findByScheduleIdAndMemberId(Long schedule, Long id);

}


