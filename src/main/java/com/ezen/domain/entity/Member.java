package com.ezen.domain.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Member extends BaseTimeEntity {

    // [회원 고유 식별 번호]
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "memberNo")
    private int memberNo;

    // [회원 아이디]
    @Column(name = "memberId")
    private String memberId;

    // [회원 비밀번호]
    @Column(name = "memberPassword")
    private String memberPassword;

    // [회원 이름]
    @Column(name = "memberName")
    private String memberName;

    // [회원 별명]
    @Column(name = "memberPseudo")
    private String memberPseudo;

    // [회원 대표 사진]
    @Column(name = "memberImg")
    private String memberImg;

    // [회원 이메일]
    @Column(name = "memberEmail")
    private String memberEmail;

    // [회원 성별]
    @Column(name = "memberGender")
    private String memberGender;

    // [회원 나이]
    @Column(name = "memberAge")
    private int memberAge;

    // [회원 등급]
    // * 추후 결제 시스템 도입 후 silver, gold, vip 로 나눌 예정
    @Column(name = "memberGrade")
    private String memberGrade;

    // [회원 포인트]
    // 충전할 때, 강의를 수강 할 때 +1000 씩 포인트 쌓이는 시스템
    @Column(name = "memberPoint")
    private int memberPoint;

    // 룸 리스트
//    @OneToMany(mappedBy="memberEntity")
//    private List<RoomEntity> roomEntities = new ArrayList<>();

}
