package com.ezen.domain.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "roomimgentity")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "roomentity")
@Setter
@Builder
@Getter
public class RoomImgEntity extends BaseTimeEntity {

    // 번호
    @Id // pk[기본키 : 테이블 1개당 기본키 1개 권장]
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동번호키
    @Column(name = "rImgNo") // 필드 속성(name)
    private int rImgNo;

    // 이미지경로
    @Column(name = "rImg")
    private String rImg;

    // 룸 관계
    @ManyToOne
    @JoinColumn(name = "roomNo")
    private RoomEntity roomEntity;
}
