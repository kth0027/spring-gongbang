package com.ezen.domain.entity;

import lombok.*;

import javax.persistence.*;

/*
 * @Author : 김정진
 * @Note : 강좌 정보를 그대로 가져가면서 시간, 날짜를 다르게 해서 DB에 저장하기 위해서 새로 만들었습니다.
 * @Date : 2022-01-27
 * 1. 클래스를 묶어주는 시간, 날짜 데이터를 포함한 테이블입니다.
 * 2. 강의를 개설할 때 시간, 날짜를 선택하지 않습니다.
 * 3. 강좌 개설 후
 * */

@Entity
@Table(name = "roomdate")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RoomDateEntity extends BaseTimeEntity {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int roomDateNo;

}
