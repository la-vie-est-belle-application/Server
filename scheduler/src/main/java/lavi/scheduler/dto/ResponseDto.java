package lavi.scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private String message;
    private Boolean result;
    private T data;

    public ResponseDto(String message, Boolean result) {
        this.message = message;
        this.result = result;
    }
}
