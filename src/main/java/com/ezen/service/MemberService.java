package com.ezen.service;


import com.ezen.domain.dto.MemberDto;
import com.ezen.domain.entity.MemberEntity;
import com.ezen.domain.entity.RoomEntity;
import com.ezen.domain.entity.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    MemberRepository memberRepository;

    // 회원등록 메소드
    public boolean membersignup(MemberDto memberDto) {
        memberRepository.save(memberDto.toentity());  // save(entity) : insert / update :  Entity를 DB에 저장
        return true;
    }

    // 이메일 중복체크
    public boolean emailcheck(String memail) {
        // 1. 모든 엔티티 가져오기
        List<MemberEntity> memberEntities = memberRepository.findAll();
        // 2. 모든 엔티티 반복문 돌려서 엔티티 하나씩 가쟈오기
        for (MemberEntity memberEntity : memberEntities) {
            // 3. 해당 엔티티가 입력한 아이디와 동일하면
            if (memberEntity.getMemberEmail().equals(memail)) {
                return true; // 중복
            }
        }
        return false; // 중복 없음
    }

    // 회원 로그인 메소드
    public MemberDto login(MemberDto memberDto) {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        for (MemberEntity memberEntity : memberEntityList) {
            if (memberEntity.getMemberEmail().equals(memberDto.getMemberEmail()) &&
                    memberEntity.getMemberPassword().equals(memberDto.getMemberPassword())) {
                return MemberDto.builder()
                        .memberEmail(memberEntity.getMemberEmail())
                        .memberNo(memberEntity.getMemberNo()).build();
            }
        }
        return null;
    }

    // 회원 번호로 회원 entity 가져오기
    public MemberEntity getMember(int memberNo){
        Optional<MemberEntity> entityOptional = memberRepository.findById(memberNo);
        return entityOptional.get();
    }

    // 회원번호 -> 회원정보 반환
    public MemberDto getmemberDto( int mnum ){
        // memberRepository.findAll(); : 모든 엔티티 호출
        // memberRepository.findById( pk값 ) : 해당 pk값의 엔티티 호출
        // 1. 해당 회원번호[pk] 만 엔티티 호출
        Optional<MemberEntity> memberEntity = memberRepository.findById(mnum);
        // 2. 찾은 entity를 dto 변경후 반환 [ 패스워드 , 수정날짜 제외 ]
        return MemberDto.builder()
                .memberName( memberEntity.get().getMemberName() )
                .memberEmail( memberEntity.get().getMemberEmail() )
                .memberPhone( memberEntity.get().getMemberPhone() )
                .memberPoint( memberEntity.get().getMemberPoint() )
                .memberGender( memberEntity.get().getMemberGender() )
                .createdDate( memberEntity.get().getCreatedDate() )
                .build();
    }

    // 회원탈퇴
    @Transactional
    public boolean delete( int mnum , String passwordconfirm ){
        // 1. 로그인된 회원번호의 엔티티[레코드] 호출
        Optional<MemberEntity> entityOptional = memberRepository.findById(mnum);
        // Optional 클래스 :  null 포함 객체 저장
        // 2. 해당 엔티티내 패스워드가 확인패스워드와 동일하면
        if( entityOptional.get().getMemberPassword().equals( passwordconfirm) ){
            // Optional 클래스 -> memberEntity.get()  :  Optional 내 객체 호출
            memberRepository.delete( entityOptional.get() );
            return true;    // 회원탈퇴
        }
        return false;  // 회원탈퇴 X
    }

    // 회원 이메일 찾기
    public String findemail(MemberDto memberDto) {
        // 1. 모든 엔티티 호출
        List<MemberEntity> memberEntities = memberRepository.findAll();
        // 2. 반복문 이용한 모든 엔티티를 하나씩 꺼내보기
        for (MemberEntity memberEntity : memberEntities) {
            // 3. 만약에 해당 엔티티가 이름과 이메일이 동일하면
            if (memberEntity.getMemberName().equals(memberDto.getMemberName()) &&
                    memberEntity.getMemberPhone().equals(memberDto.getMemberPhone())) {
                // 4. 아이디를 반환한다
                return memberEntity.getMemberEmail();
            }
        }
        // 5. 만약에 동일한 정보가 없으면
        return null;
    }

    // 비밀번혼
    public String findpassword(MemberDto memberDto) {
        // 1. 모든 엔티티 호출
        List<MemberEntity> memberEntities = memberRepository.findAll();
        // 2. 반복문 이용한 모든 엔티티를 하나씩 꺼내보기
        for (MemberEntity memberEntity : memberEntities) {
            // 3. 만약에 해당 엔티티가 이름과 이메일이 동일하면
            if (memberEntity.getMemberEmail().equals(memberDto.getMemberEmail()) &&
                    memberEntity.getMemberPhone().equals(memberDto.getMemberPhone())) {
                // 4. 패스워드를 반환한다
                return memberEntity.getMemberPassword();
            }
        }
        // 5. 만약에 동일한 정보가 없으면
        return null;
    }

    // 내가등록한 클래스
//    public List<RoomEntity> getMyClass(int memberNo) {
//
//    }

}
