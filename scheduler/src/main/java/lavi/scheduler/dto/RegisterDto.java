package lavi.scheduler.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RegisterDto {
    private Long id;    //회원 번호
    private List<LocalDate> registerDateList;   //출근 가능 날짜 리스트
    private LocalDate workingDate;
}
