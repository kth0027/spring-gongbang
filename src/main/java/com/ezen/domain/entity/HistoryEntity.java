package com.ezen.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "history")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class HistoryEntity extends BaseTimeEntity{

    /*
    * 회원의 이전 수강 신청 내역, 댓글 남김 내역을 담는 엔티티
    * */

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int historyNo;

    // 02-08 클래스 수강 조지훈
    @ManyToOne
    @JoinColumn(name = "memberNo")
    private MemberEntity memberEntity;
    // 02-08 클래스 수강 조지훈
    @ManyToOne
    @JoinColumn(name = "roomNo")
    private RoomEntity roomEntity;



}
