package lavi.scheduler.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity
@Getter
@AllArgsConstructor
public class Member {

    @Id
    @Column( name = "member_id")
    private Long id;
    private String name;
    private String eMail;
    private int pay;
    private String gender;

    @Enumerated(EnumType.STRING)
    private RollType rollType;

    public Member() {

    }
}
