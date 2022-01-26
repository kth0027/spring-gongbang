package com.ezen.service;

import com.ezen.domain.entity.RoomEntity;
import com.ezen.domain.entity.repository.RoomImgRepository;
import com.ezen.domain.entity.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    HttpServletRequest request;
    @Autowired
    private MemberService memberService;


    // 모든 룸 가져오기
    public List<RoomEntity> getroomlist(){
        return roomRepository.findAll();


    }

}
