package lavi.scheduler.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue
    @Column( name = "member_id")
    private Long id;
    private String kakaoId;
    private String name;
    private String eMail;
    private int pay;
    private String gender;

    @Enumerated(EnumType.STRING)
    private RollType rollType;

    public Member(String kakaoId, String name, String eMail, String gender) {
        this.kakaoId = kakaoId;
        this.name = name;
        this.eMail = eMail;
        this.pay = 10000;
        this.gender = gender;
        this.rollType = RollType.User;
    }

    public Member() {

    }
}
