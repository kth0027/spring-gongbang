package com.ezen.domain.dto;

import com.ezen.domain.entity.MemberEntity;
import com.ezen.domain.entity.Role;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@ToString
@Builder
public class MemberDto {
    // 필드
    private int memberNo;   // 회원번호
    private String memberId;
    private String memberPassword; // 회원비밀번호
    private String memberName; // 회원이름
    private String memberGender; // 회원성별
    private String memberPhone; // 회원연락처
    private String memberEmail; // 회원이메일
    private Role memberGrade;
    private int memberPoint; // 회원포인트
    private LocalDateTime createdDate; // 회원가입 날자

    // Dto -> entity
    public MemberEntity toentity(){
        return MemberEntity.builder()
                .memberPassword(this.memberPassword)
                .memberId( this.memberId)
                .memberName( this.memberName)
                .memberGender( this.memberGender )
                .memberPhone( this.memberPhone)
                .memberEmail( this.memberEmail)
                .memberGrade(Role.MEMBER)
                .memberPoint(this.memberPoint)
                .build();
    }

}


