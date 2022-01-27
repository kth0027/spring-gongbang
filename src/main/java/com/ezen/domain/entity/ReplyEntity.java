package com.ezen.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "reply")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ReplyEntity extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="replyNo")
    private int replyNo;

    @Column(name="replyContent")
    private String replyContent;

     // 후기 / 문의 로 나누어 쓸까 생각
    @Column(name="replyCategory")
    private int replyCategory;


    @ManyToOne
    @JoinColumn(name="roomNo") // 해당 필드의 이름[컬럼 = 열 = 필드]
    private RoomEntity roomEntity;

    @ManyToOne
    @JoinColumn(name="memberNo") // 해당 필드의 이름[컬럼 = 열 = 필드]
    private MemberEntity memberEntity;




}
