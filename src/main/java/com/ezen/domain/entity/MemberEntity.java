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

    // 회원이 여러개의 방을 등록할 수 있다.
    // RoomEntity 와 1 : N 관계를 맺는다.
    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<RoomEntity> roomEntities = new ArrayList<>();

    // 문의 리스트
    @OneToMany(mappedBy="memberEntity")
    @ToString.Exclude
    private List<NoteEntity> noteEntities = new ArrayList<>();

}
