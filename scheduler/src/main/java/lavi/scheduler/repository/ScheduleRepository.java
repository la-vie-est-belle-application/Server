package lavi.scheduler.repository;

import lavi.scheduler.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Schedule findByWorkingDate(LocalDate date);

    boolean existsByWorkingDate(LocalDate workingDate);

    List<Schedule> findAllByWorkingDateIn(Set<LocalDate> dates);

//    Schedule updateScheduleByWorkingDate(LocalDate workingDate);

}
