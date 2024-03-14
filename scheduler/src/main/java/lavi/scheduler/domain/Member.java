package lavi.scheduler.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter

public class Member {

    @Id
    @GeneratedValue
    @Column( name = "member_id")
    private Long id;
    private String name;
    private String eMail;
    private int pay;
    private String gender;
    private String token;

    @Enumerated(EnumType.STRING)
    private RollType rollType;

}
