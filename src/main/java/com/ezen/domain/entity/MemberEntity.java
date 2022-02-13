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
public class MemberEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "memberNo")
    private int memberNo;

    @Column(name="memberEmail")
    private String memberEmail;

    @Column(name = "memberPassword")
    private String memberPassword;

    @Column(name = "memberName")
    private String memberName;

    @Column(name = "memberPhone")
    private String memberPhone;

    @Column(name="memberGender")
    private String memberGender;

    @Column(name="memberPoint")
    private int memberPoint;

    @Column(name="memberId")
    private String memberId;


    // 회원이 여러개의 방을 등록할 수 있다.
    // RoomEntity 와 1 : N 관계를 맺는다.
    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<RoomEntity> roomEntities = new ArrayList<>();

    // 문의 리스트
    @OneToMany(mappedBy="memberEntity")
    @ToString.Exclude
    private List<NoteEntity> noteEntities = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column
    private Role memberGrade; // 회원등급
    public String getRolekey(){
        return this.memberGrade.getKey();
    }

    // oauth2에서 동일한 이메일이면 업데이트 처리 메소드
    public MemberEntity update(String name){
        this.memberName = name;
        return this;
    }

    // 리뷰 리스트
    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ReplyEntity> replyEntities = new ArrayList<>();

    // 회원은 여러개의 예약 내역을 가질 수 있습니다.
    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<HistoryEntity> historyEntities = new ArrayList<>();


}
