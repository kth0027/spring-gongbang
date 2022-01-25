package com.ezen.domain.entity;

import lombok.*;

import javax.persistence.*;

/*
 * 현재 개설중인 공방 리스트를 출력합니다.
 *
 * */
@Entity
@Table(name = "room")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RoomEntity extends BaseTimeEntity {

    // [클래스 고유 식별 번호]
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int roomNo;

    // [클래스 명]
    @Column(name = "roomTitle")
    private String roomTitle;

    // [클래스 카테고리(베이킹, 음악, 등등 분류)]
    @Column(name = "roomCategory")
    private String roomCategory;

    // [클래스 설명]
    @Column(name = "roomContent")
    private String roomContent;

    // [클래스 진행 예정 날짜]
    // 2021-01-30,15:00,17:00
    // 날짜, 시작 시간, 끝나는 시간을 한번에 저장합니다.
    @Column(name = "roomDate")
    private String roomDate;

    // [클래스 주소]
    // [도로명 주소],[위도],[경도]
    // 위 조합으로 한꺼번에 db에 저장합니다.
    @Column(name = "roomAddress")
    private String roomAddress;

    // [클래스 상태]
    // 0 : 승인 대기 중
    // 1 : 승인, 모집 중
    // 2 : 시작하지 않았지만 정원이 꽉 찼음
    // 3 : 지정된 시간이 끝나서 더 이상 참여 및 수정이 불가능함
    @Column(name = "roomStatus")
    private int roomStatus;

    // [클래스 정원]
    // 개발자 마음대로 1명~6명으로 제한을 걸겠습니다.
    @Column(name = "roomMax")
    private int roomMax;

    /*
     * 추가 사항
     * MemberRef 엔티티와 @ManyToOne 관계를 맺습니다.
     * RoomImg 엔티티와 @OneToMany 관계를 맺습니다.
     * */

//     회원번호 관계
    @ManyToOne
    @JoinColumn(name="memberNo") // 해당 필드의 이름[컬럼 = 열 = 필드]
    private MemberEntity memberEntity;

    // 이미지 관계
//    @OneToMany(mappedBy="room")
//    private List<RoomImgEntity> roomImgEntities = new ArrayList<>();


}
