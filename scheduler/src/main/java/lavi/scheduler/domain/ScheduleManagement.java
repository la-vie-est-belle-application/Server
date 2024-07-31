package lavi.scheduler.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ScheduleManagement {

    @Id
    @GeneratedValue
    @Column( name = "scheduleManagement_id")
    private Long id;

    @ManyToOne
    @JoinColumn( name = "schedule_id")
    private Schedule schedule;

    @ManyToOne
    @JoinColumn( name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private Position position;

    @Enumerated(EnumType.STRING)
    private RequestResult requestResult;


    public ScheduleManagement(Schedule schedule, Member member) {
        this.schedule = schedule;
        this.member = member;
        this.requestResult = RequestResult.WAIT;
    }

    public ScheduleManagement() {

    }

    public void updateSchedule(Position position) {
        this.position = position;
        this.requestResult = RequestResult.ACCEPT;
    }
}
