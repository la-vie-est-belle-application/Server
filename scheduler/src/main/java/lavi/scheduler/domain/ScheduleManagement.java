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

    private int managementStatus;
}
