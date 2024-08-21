package lavi.scheduler.dto;

import lavi.scheduler.domain.Schedule;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ScheduleUpdateDto {
    private Schedule scheduleDto;
    private Map<String, List<MemberPositionDto>> positionDto;
}
