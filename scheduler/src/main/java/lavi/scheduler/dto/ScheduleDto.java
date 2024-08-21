package lavi.scheduler.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ScheduleDto {
    private LocalDate workingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean scheduleStatus;
}
