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
public class MemberEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "memberNo")
    private int mnum;

    @Column(name="memberEmail")
    private String memail;

    @Column(name = "memberPassword")
    private String mpassword;

    @Column(name = "memberName")
    private String mname;

    @Column(name = "memberPhone")
    private String mphone;

    @Column(name="memberGender")
    private String msex;

    @Column(name="memberPoint")
    private int mpoint;

}
