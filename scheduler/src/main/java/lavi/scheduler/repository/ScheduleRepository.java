package lavi.scheduler.repository;

import lavi.scheduler.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Schedule findByWorkingDate(LocalDate date);

    boolean existsByWorkingDate(LocalDate workingDate);
}
