package com.ezen.service;

import com.ezen.domain.dto.MemberDto;
import com.ezen.domain.entity.RoomEntity;
import com.ezen.domain.entity.repository.MemberRepository;

import com.ezen.domain.entity.repository.RoomImgRepository;
import com.ezen.domain.entity.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpSession;

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

    public List<RoomEntity> getmyroomlist() {

        HttpSession session = request.getSession();
        MemberDto logindto = (MemberDto)session.getAttribute("logindto");

        List<RoomEntity> roomEntities = memberRepository.findById( logindto.getMemberNo()).get().getRoomEntities();

        return roomEntities;
    }



    // 모든 룸 가져오기
    public List<RoomEntity> getroomlist(){
        return roomRepository.findAll();


    }

}
