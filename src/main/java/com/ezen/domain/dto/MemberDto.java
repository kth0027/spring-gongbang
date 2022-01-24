package com.ezen.domain.dto;


import com.ezen.domain.entity.MemberEntity;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
@ToString
@Builder
public class MemberDto {
    // 필드
    private int mnum;   // 회원번호
    private String mpassword; // 회원비밀번호
    private String mname; // 회원이름
    private String msex; // 회원성별
    private String mphone; // 회원연락처
    private String memail; // 회원이메일
    private int mpoint; // 회원포인트
    private LocalDateTime mcreatedDate; // 회원가입 날자

    // Dto -> entity
    public MemberEntity toentity(){
        return MemberEntity.builder()
                .mpassword(this.mpassword)
                .mname( this.mname)
                .msex( this.msex )
                .mphone( this.mphone)
                .memail( this.memail)
                .mpoint( this.mpoint)
                .build();
    }

}


