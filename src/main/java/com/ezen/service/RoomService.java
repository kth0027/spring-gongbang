package com.ezen.service;

import com.ezen.domain.dto.MemberDto;

import com.ezen.domain.entity.MemberEntity;
import com.ezen.domain.entity.RoomEntity;
import com.ezen.domain.entity.RoomImgEntity;
import com.ezen.domain.entity.repository.MemberRepository;
import com.ezen.domain.entity.repository.RoomImgRepository;
import com.ezen.domain.entity.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


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
    @Transactional
    public boolean registerClass(RoomEntity roomEntity, List<MultipartFile> files){
        System.out.println(roomEntity.getRoomNo());
        System.out.println(roomEntity.getRoomAddress());
        System.out.println(roomEntity.getRoomCategory());

        // 1. 등록하려는 회원 번호 : 세션 정보
        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        MemberEntity memberEntity = memberService.getMember(memberDto.getMemberNo());
        // 2. room entity 에 member entity 를 추가시킨다.
        roomEntity.setMemberEntity(memberEntity);
        // 3. room entity 저장 후 roomNo 를 가져온다.
        int roomNo = roomRepository.save(roomEntity).getRoomNo();
        // 4. member entity 에 room entity 저장
        RoomEntity roomEntitySaved = roomRepository.findById(roomNo).get();
        // 4.1 member entity 에 방금 저장된 room entity 를 저장시킨다.
        // 4.2 memberEntity 에는 @OneToMany 형태로 맵핑되어있다.
        // 4.3 member 1명이 여러개의 room 을 등록할 수 있고, 등록할 시 맵핑을 시켜주는 역할이다.
        memberEntity.getRoomEntities().add(roomEntitySaved);

        // 5. 이미지 처리
        String uuidfile = null;
        if(files.size()!=0){
            for(MultipartFile file : files){
                // 1. 난수 + '_' + 파일이름
                UUID uuid = UUID.randomUUID();
                uuidfile = uuid.toString() + "_" + Objects.requireNonNull(file.getOriginalFilename()).replaceAll("_","-");
                // 2. 저장될 경로
                String dir = "C:\\Users\\PC\\IdeaProjects\\gongbang\\src\\main\\resources\\static\\roomimg";
                // 3. 저장될 파일의 전체 [현재는 절대]경로
                String filepath = dir + "\\" + uuidfile;
                try{
                    // 4. 지정한 경로에 파일을 저장시킨다.
                    file.transferTo(new File(filepath));
                } catch(Exception e){
                    System.out.println("오류 : " + e);
                }
                // 5.entity 에 파일 경로를 저장한다.
                // 5.1 roomEntity 에 room entity 를 주입해야한다.
                RoomImgEntity roomImgEntity = RoomImgEntity.builder()
                        .roomImg(uuidfile)
                        .roomEntity(roomEntitySaved)
                        .build();

                // 6. 각각의 파일을 repo 를 통해 db에 저장한다.
                // 6.1 해당하는 파일의 roomImgNo 를 통해 해당하는 이미지를 불러온다.
                int roomImgNo = roomImgRepository.save(roomImgEntity).getRoomImgNo();
                RoomImgEntity roomImgEntitySaved = roomImgRepository.findById(roomImgNo).get();
                // 6.2 각각의 이미지를 room entity 에 선언된 list 에 저장시킨다.
                roomEntitySaved.getRoomImgEntities().add(roomImgEntitySaved);
            }
        }
        return true;
    }
    public Page<RoomEntity> getmyroomlist(Pageable pageable) {

        //페이지번호
        int page = 0;
        if (pageable.getPageNumber() == 0) page = 0; // 0이면1페이지
        else page = pageable.getPageNumber() - 1; // 1이면 -1 => 1페이지  // 2이면-1 => 2페이지
        //페이지 속성[PageRequest.of(페이지번호, 페이지당 게시물수, 정렬기준)]
        pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "roomNo")); // 변수 페이지 10개 출력
        return roomRepository.findAll(pageable);
    }

    // 내가 만든 room list 가져오기
//    public List<RoomEntity> getmyroomlist() {
//
//        HttpSession session = request.getSession();
//        MemberDto logindto = (MemberDto)session.getAttribute("logindto");
//
//        List<RoomEntity> roomEntities = memberRepository.findById( logindto.getMemberNo()).get().getRoomEntities();
//
//        return roomEntities;
//    }

    // room 상세페이지
    public RoomEntity getroom(int roomNo) {
        return roomRepository.findById(roomNo).get();
    }

    // 모든 룸 가져오기
    public List<RoomEntity> getroomlist(){
        return roomRepository.findAll();
    }


}
