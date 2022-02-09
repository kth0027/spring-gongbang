package com.ezen.service;

import com.ezen.domain.dto.MemberDto;
import com.ezen.domain.entity.MemberEntity;
import com.ezen.domain.entity.RoomEntity;
import com.ezen.domain.entity.RoomImgEntity;
import com.ezen.domain.entity.TimeTableEntity;
import com.ezen.domain.entity.repository.MemberRepository;
import com.ezen.domain.entity.repository.RoomImgRepository;
import com.ezen.domain.entity.repository.RoomRepository;
import com.ezen.domain.entity.repository.TimeTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    TimeTableRepository timeTableRepository;

    @Transactional
    public boolean registerClass(RoomEntity roomEntity, List<MultipartFile> files) {
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
        if (files.size() != 0) {
            for (MultipartFile file : files) {
                // 1. 난수 + '_' + 파일이름
                UUID uuid = UUID.randomUUID();
                uuidfile = uuid.toString() + "_" + Objects.requireNonNull(file.getOriginalFilename()).replaceAll("_", "-");
                // 2. 저장될 경로
                String dir = "C:\\Users\\505\\IdeaProjects\\gongbang\\src\\main\\resources\\static\\roomimg";

                /*
                * 저장되는 경로를 상대경로로 지정합니다.
                * resources - static - roomimg
                *
                * */
                // 상대 경로 지정
                String newdir = "/static/roomimg";

                // 3. 저장될 파일의 전체 [현재는 절대]경로
                String filepath = dir + "\\" + uuidfile;

                String newFilePath = newdir + "/" + uuidfile;

                try {
                    // 4. 지정한 경로에 파일을 저장시킨다.
                    file.transferTo(new File(filepath));
                } catch (Exception e) {
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

    // 내가 만든 room list 가져오기
    public List<RoomEntity> getmyroomlist() {
        HttpSession session = request.getSession();
        MemberDto logindto = (MemberDto) session.getAttribute("logindto");
        List<RoomEntity> roomEntities = memberRepository.findById(logindto.getMemberNo()).get().getRoomEntities();
        return roomEntities;
    }

    // 검색 결과 room list
    public List<RoomEntity> getRoomEntityBySearch(String keyword, String local, String category) {
        // 1.1 검색이 없는 경우
        if (keyword.equals("")) {
            // 1.2 지역은 선택하고 카테고리는 선택하지 않았을 경우
            if (local != null && category.equals("")) {
                // 1.2.1 지역만을 인수로 넘긴다.
                return roomRepository.findRoomByLocal(local);
            }
            // 1.3 지역은 선택하지 않고 카테고리는 선택했을 경우
            else if (local.equals("") && category != null) {
                // 1.3.1 카테고리만을 인수로 넘긴다.
                return roomRepository.findRoomByCategory(category);
            }
            // 1.4 지역과 카테고리를 모두 선택했을 경우
            else if (local != null && category != null) {
                // 1.4.1 지역, 카테고리를 인수로 넘긴다.
                return roomRepository.findRoomByLocalAndCategory(local, category);
            }
            // 1.5 검색 X 카테고리 X 지역 X 인 경우에는 등록된 클래스 전체를 리턴한다.
            else if (local.equals("") && category.equals("")){
                return roomRepository.findAll();
            }
        }
        // 2. 검색이 있는 경우
        else {
            // 검색 O 지역 O 카테고리 X
            if (local != null && category.equals("")) {
                return roomRepository.findRoomByKeyword(keyword);
            }
            // 검색 O 지역 X 카테고리 O
            else if (local.equals("") && category != null) {
                return roomRepository.findRoomByKeyword(keyword);
            }
            // 검색 O 지역 O 카테고리 O
            else if (local != null && category != null) {
                return roomRepository.findRoomByKeyword(keyword);
            }
            // 검색 O 지역 X 카테고리 X
            else if (local.equals("") && category.equals("")) {
                return roomRepository.findRoomByKeyword(keyword);
            }
        }
        return null;
    }

    // room 상세페이지
    public RoomEntity getroom(int roomNo) {
        return roomRepository.findById(roomNo).get();
    }

    // 모든 룸 가져오기
    public List<RoomEntity> getroomlist() {
        return roomRepository.findAll();
    }

    // 룸에 날짜, 시간 지정하기
    @Transactional
    public boolean registerTimeToClass(TimeTableEntity timeTableEntity, int roomNo) {
        if (roomRepository.findById(roomNo).isPresent()) {
            RoomEntity roomEntity = roomRepository.findById(roomNo).get();
            timeTableEntity.setRoomEntity(roomEntity);
            // room 엔티티에 timeTableEntity 추가
            roomEntity.getTimeTableEntity().add(timeTableEntity);
            // room 리스트에 room 을 추가
            // 작성된 시간 엔티티를 db 에 추가시킨다.
            timeTableRepository.save(timeTableEntity);
            return true;
        } else {
            return false;
        }
    }

    // 메인 화면에 등록된 강좌 출력
    // 가장 최근에 강의가 개설된 강좌 6개만 출력합니다.
    // 아니 그냥 대칭으로 보기 이쁘게 9개 출력합니다.
    public List<RoomEntity> getRoomEntityInMain(){
        // 1. 가장 최근에 등록한 강좌를 TableEntity 에서 빼옵니다.
        List<TimeTableEntity> roomEntities = timeTableRepository.getByTimeSequence();
        // 2. RoomEntity 를 저장하는 리스트를 생성해서 집어넣습니다. 9개가 되면 종료 !
        int count = 0;

        return null;


    }

}
