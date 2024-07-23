package lavi.scheduler.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserSession {
    private Long id;
    private String name;
    private RollType rollType;
}
