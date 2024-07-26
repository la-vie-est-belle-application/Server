package lavi.scheduler.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
public class Schedule {

    @Id @GeneratedValue
    @Column( name = "schedule_id")
    private Long id;
    private LocalDate workingDate;
    private LocalTime startTime;
    private LocalTime endTime;

    public Schedule(LocalDate workingDate) {
        this.workingDate = workingDate;
    }

    public Schedule() {

    }

    public void update(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
