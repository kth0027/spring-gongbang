package com.ezen.domain.entity;

import lombok.*;

import javax.persistence.*;

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
    // ex) 15:00,17:00
    @Column(name = "roomTime")
    private String roomTime;

    // RoomEntity 와 1 : N 관계
    @ManyToOne
    @JoinColumn(name="roomNo")
    private RoomEntity roomEntity;


}
