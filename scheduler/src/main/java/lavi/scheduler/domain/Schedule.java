package lavi.scheduler.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
public class Schedule {

    @Id @GeneratedValue
    @Column( name = "schedule_id")
    private Long id;
    private LocalDate workingDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Schedule() {

    }
}
