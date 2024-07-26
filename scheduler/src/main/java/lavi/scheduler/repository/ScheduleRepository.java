package lavi.scheduler.repository;

import lavi.scheduler.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Schedule findByWorkingDate(LocalDate workingDate);

    boolean existsByWorkingDate(LocalDate workingDate);
}
