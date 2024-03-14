package lavi.scheduler.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Notice {

    @Id
    @GeneratedValue
    @Column( name = "notice_id")
    private Long id;

    @ManyToOne
    @JoinColumn( name = "member_id")
    private Member member;

    private String title;
    private String content;
    private LocalDateTime enrollDate;
    private int inquiry;

    @Enumerated(EnumType.STRING)
    private Category category;
}
