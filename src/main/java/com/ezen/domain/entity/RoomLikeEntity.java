package com.ezen.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "roomlike")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RoomLikeEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int rookLikeNo;

    // 회원 : 좋아요 = 1 : 1
    @OneToOne()
    @JoinColumn(name = "memberNo")
    private MemberEntity memberEntity;

    // 강의 : 좋아요 = 1 : 1
    @OneToOne()
    @JoinColumn(name = "roomNo")
    private RoomEntity roomEntity;
}
