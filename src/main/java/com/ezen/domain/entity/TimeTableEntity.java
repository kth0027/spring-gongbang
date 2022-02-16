package com.ezen.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "timetable")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TimeTableEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int timeTableNo;

    // YY-mm-DD
    @Column(name = "roomDate")
    private String roomDate;

    // 시작시간, 끝나는 시간
    // ex) 15,17
    @Column(name = "roomTime")
    private String roomTime;

    // RoomEntity 와 1 : N 관계
    @ManyToOne
    @JoinColumn(name = "roomNo")
    private RoomEntity roomEntity;

    // TimeTable Entity 에는 History Entity 가 다수 들어갈 수 있다.
    // mappedBy 는 '부모' 가 갖는다.
    @OneToMany(mappedBy = "timeTableEntity", cascade = CascadeType.ALL)
    private List<HistoryEntity> historyEntity;

}
