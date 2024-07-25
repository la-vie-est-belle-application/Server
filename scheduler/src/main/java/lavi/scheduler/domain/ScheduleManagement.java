package lavi.scheduler.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ScheduleManagement {

    @Id
    @GeneratedValue
    @Column( name = "management_id")
    private Long id;

    @ManyToOne
    @JoinColumn( name = "schedule_id")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn( name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private Position position;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean managementStatus;       //true = 승인 = 1 , false = 거절(신청상태) = 0

    public ScheduleManagement(Schedule schedule, Member member) {
        this.schedule = schedule;
        this.member = member;
        this.managementStatus = false;
    }

    public ScheduleManagement() {

    }
}
