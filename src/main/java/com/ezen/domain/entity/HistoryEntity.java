package com.ezen.domain.entity;

import lombok.*;

import javax.persistence.*;

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


}
