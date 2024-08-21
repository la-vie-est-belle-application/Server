package lavi.scheduler.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RegisterScheduleDto {
    private List<LocalDate> workingDate;
}
