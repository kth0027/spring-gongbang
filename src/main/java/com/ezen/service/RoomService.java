package com.ezen.service;

import com.ezen.domain.entity.RoomEntity;
import com.ezen.domain.entity.repository.MemberRepository;
import com.ezen.domain.entity.repository.RoomImgRepository;
import com.ezen.domain.entity.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;

import java.util.List;

@Service
public class RoomService {
    @Autowired
    private RoomImgRepository roomImgRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    HttpServletRequest request;
    @Autowired
    private MemberService memberService;

/*    public List<RoomEntity> getmyroomlist() {

        HttpSession session = request.getSession();
        MemberDto logindto = (MemberDto)session.getAttribute("logindto");

        List<RoomEntity> roomEntities = memberRepository.findById( logindto.getMemberNo()).get().getRoomEntities();

        return roomEntities;
    }*/

    public Page<RoomEntity> getmyroomlist(Pageable pageable){

        //페이지번호
        int page = 0;
        if(pageable.getPageNumber()==0) page=0; // 0이면1페이지
        else page = pageable.getPageNumber()-1; // 1이면 -1 => 1페이지  // 2이면-1 => 2페이지
        //페이지 속성[PageRequest.of(페이지번호, 페이지당 게시물수, 정렬기준)]
        pageable = PageRequest.of(page,5, Sort.by(Sort.Direction.DESC,"roomNo")); // 변수 페이지 10개 출력


        return roomRepository.findAll(pageable);
    }


    // 모든 룸 가져오기
    public List<RoomEntity> getroomlist(){
        return roomRepository.findAll();


    }

}
