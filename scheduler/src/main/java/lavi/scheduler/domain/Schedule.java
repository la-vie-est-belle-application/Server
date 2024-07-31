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

    @Column(columnDefinition = "TINYINT(1)")
    private Boolean scheduleStatus;     // 0:신청 마감 , 1: 신청 가능

    public Schedule(LocalDate workingDate) {
        this.workingDate = workingDate;
        this.scheduleStatus = true;
    }

    public Schedule() {

    }

    public void updateTime(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
