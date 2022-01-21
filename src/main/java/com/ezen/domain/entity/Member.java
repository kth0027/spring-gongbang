package com.ezen.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "member")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "memberNo")
    private int memberNo;

    @Column(name = "memberId")
    private String memberId;

    @Column(name = "memberPassword")
    private String memberPassword;

    @Column(name = "memberName")
    private String memberName;

    @Column(name = "memberPseudo")
    private String memberPseudo;

    @Column(name="memberImg")
    private String memberImg;

    @Column(name="memberEmail")
    private String memberEmail;

    @Column(name="memberGender")
    private String memberGender;

    @Column(name="memberAge")
    private int memberAge;

    @Column(name="memberGrade")
    private String memberGrade;

    @Column(name="memberPoint")
    private int memberPoint;


}
