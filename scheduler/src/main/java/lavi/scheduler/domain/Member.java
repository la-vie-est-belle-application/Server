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

    @Column(columnDefinition = "TINYINT(1)")
    private boolean confirm;      //0:false, 1:true

    @Column(columnDefinition = "TINYINT(1)")
    private boolean rollType;       //true = 관리자 = 1 , false = 알바생 = 0

    public Member(String kakaoId, String name, String eMail, String gender) {
        this.kakaoId = kakaoId;
        this.name = name;
        this.eMail = eMail;
        this.pay = 10000;
        this.gender = gender;
        this.rollType = false;
        this.confirm = false;
    }

    public Member() {

    }
}
