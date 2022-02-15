package com.ezen.controller;

import com.ezen.domain.dto.MemberDto;
import com.ezen.domain.entity.HistoryEntity;
import com.ezen.domain.entity.MemberEntity;
import com.ezen.domain.entity.RoomEntity;
import com.ezen.domain.entity.TimeTableEntity;
import com.ezen.domain.entity.repository.*;
import com.ezen.service.MemberService;
import com.ezen.service.RoomService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;

@RequestMapping("/member")
@Controller
public class MemberController { // C S

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private TimeTableRepository timeTableRepository;

    // 예약 내역을 저장할 Repo
    // 예약 내역(=HistoryEntity) 는 따로 Controller, Service 를 만들지 않습니다.
    // 예약 내역과 관련된 것은 모두 MemberController, ServiceController 에 선언합니다.
    @Autowired
    private HistoryRepository historyRepository;

    // 클래스 장바구니
    @Autowired
    private RoomLikeRepository roomLikeRepository;

    // 회원가입페이지 연결
    @GetMapping("/signup")
    public String signup() {
        return "member/signup";
    }

    // 회원가입 처리 연결
    @PostMapping("/signupController") // 회원가입 처리 연결
    public String signupController(MemberDto memberDto
    ) {
        memberService.memberSignup(memberDto);
        return "redirect:/";  // 회원가입 성공시 메인페이지 연결
    }

    // 이메일 중복체크
    @GetMapping("/emailcheck")
    @ResponseBody
    public String emailcheck(@RequestParam("memberEmail") String memail) {
        boolean result = memberService.emailcheck(memail);
        if (result) {
            return "1"; // 중복
        } else {
            return "2"; // 중복x
        }
    }

    // 아이디 중복체크
    @GetMapping("idCheck")
    @ResponseBody
    public String idCheck(@RequestParam("memberId") String memberId) {
        boolean result = memberService.idCheck(memberId);
        if (result) {
            return "1";
        } else {
            return "2";
        }
    }

    // 로그인페이지 연결
    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

    // 마이페이지 연결
    @GetMapping("/info")
    public String info(Model model) {

        // 1. 로그인 세션 호출
        HttpSession session = request.getSession();
        MemberDto loginDto = (MemberDto) session.getAttribute("logindto");

        // 2. 세션에 회원정보를 service 에 전달해서 동일한 회원번호에 회원정보 가져오기
        MemberDto memberDto = memberService.getmemberDto(loginDto.getMemberNo());

        // 3. 찾은 회원정보를 model 인터페이스를 이용한 view 전달하기
        model.addAttribute("memberDto", memberDto);

        return "member/info";
    }

    // 회원삭제 처리
    @GetMapping("/mdelete")
    @ResponseBody
    public int mdelete(@RequestParam("passwordconfirm") String passwordconfirm) {

        // 1. 세션 호출
        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        // 2. service에 로그인된 회원번호 , 확인패스워드
        boolean result = memberService.delete(memberDto.getMemberNo(), passwordconfirm);
        // 3. 결과 를 ajax에게 응답
        if (result) {
            return 1;
        } else {
            return 2;
        }
    }

    // 회원정보찾기 페이지로 연결
    @GetMapping("/findemail")
    public String findemail() {
        return "member/findemail";
    }

    // 이메일 찾기
    @PostMapping("/findemailcontroller")
    public String findemailcontroller(MemberDto memberDto, Model model) {
        String result = memberService.findemail(memberDto);
        if (result != null) {
            String msg = " 회원님의 이메일 : " + result;
            model.addAttribute("findemailmsg", msg);
        } else {
            String msg = " 동일한 회원정보가 없습니다.";
            model.addAttribute("findemailmsg", msg);
        }
        return "member/findemail";
    }

    // 비밀번호 찾기
    @PostMapping("/findpasswordcontroller")
    public String findpasswordcontroller(MemberDto memberDto, Model model) {
        String result = memberService.findpassword(memberDto);
        if (result != null) {
            String msg = " 회원님의 이메일 : " + result;
            model.addAttribute("findpwmsg", msg);
        } else {
            String msg = " 동일한 회원정보가 없습니다.";
            model.addAttribute("findpwmsg", msg);
        }
        return "member/findemail";
    }

    // @Author : 김정진
    // @Date : 2022-02-11
    // @Note :
    // 1. 회원이 신청한 정보를 DB에 등록하면서 예약 처리하는 메소드입니다.
    @GetMapping("/registerClass")
    @ResponseBody
    @Transactional
    public String registerClass(@RequestParam("roomNo") int roomNo,
                                @RequestParam("roomTime") String classTime,
                                @RequestParam("roomDate") String roomDate,
                                @RequestParam("person") int person) {

        MemberEntity memberEntity = null;
        // 0. 로그인된 회원 정보를 불러온다.
        HttpSession session = request.getSession();
        MemberDto loginDto = (MemberDto) session.getAttribute("logindto");
        // 0.1 로그인 세션 정보가 없으면 메인 페이지로 이동해서 로그인을 요구한다.
        if (loginDto == null) {
            return "redirect: /index";
        } else {
            // 0.2 로그인 세션 정보가 존재하면 member Entity 를 호출한다.
            memberEntity = memberService.getMemberEntity(loginDto.getMemberNo());
        }

        RoomEntity roomEntity = null;
        TimeTableEntity timeTableTmp = null;

        // 1. roomNo 에 해당하는 room 엔티티를 호출한다.
        // 1.1 history 저장 후 room 엔티티에 선언된 list 에 histroy 를 추가시켜야한다.
        if (roomRepository.findById(roomNo).isPresent()) {
            roomEntity = roomRepository.findById(roomNo).get();
        }

        // 1.2 클래스에 등록된 최대 인원을 넘어가면 등록을 막는다.
        assert roomEntity != null;
        if (roomEntity.getRoomMax() < person) {
            return "2";
        }
        // 2. 받아온 시간으로 TimeTable 을 가져온다.
        // 2.1 TimeTable 내에서 roomTime 에 해당하는 것만 등록한다.
        List<TimeTableEntity> timeTableEntities = timeTableRepository.getTimeTableByRoomNo(roomNo);
        for (TimeTableEntity timeTableEntity : timeTableEntities) {
            if (timeTableEntity.getRoomTime().equals(classTime)) {
                timeTableTmp = timeTableEntity;
            }
        }


        // 3. HistoryEntity 에 멤버 정보, 클래스 정보를 들록합니다.
        HistoryEntity historyEntity = HistoryEntity.builder()
                .memberEntity(memberEntity)
                .roomEntity(roomEntity)
                .timeTableEntity(timeTableTmp)
                .build();


        // 4. 예약내역 저장하고 저장번호 받아오기
        int savedHistoryEntityNo = historyRepository.save(historyEntity).getHistoryNo();
        // 신청한 정원만큼 클래스 수용 인원을 감소시킵니다. 
        roomEntity.setRoomMax(roomEntity.getRoomMax() - person);
        // 수용 가능 인원이 '0' 명이 된다면, 클래스 상태를 '모집완료' 로 바꿉니다. 
        if(roomEntity.getRoomMax() == 0){
            roomEntity.setRoomStatus("모집완료");
        }

        // 5. 위에서 저장한 예약내역 가져오기
        HistoryEntity savedHistoryEntity = historyRepository.findById(savedHistoryEntityNo).get();

        // 6. historyEntity 를 TimeTable Entity 에 선언한 List<HistoryEntity> 에 추가한다.
        assert timeTableTmp != null;
        timeTableTmp.getHistoryEntity().add(savedHistoryEntity);
        memberEntity.getHistoryEntities().add(savedHistoryEntity);
        roomEntity.getHistoryEntities().add(savedHistoryEntity);

        return "1";
    }

    // [회원 예약 내역 페이지와 맵핑]
    // @Param memberNo : 회원 번호를 넘겨받는다.
    @GetMapping("/reservationListController")
    public String reservationListController(Model model) {
        HttpSession session = request.getSession();
        MemberDto loginDto = (MemberDto) session.getAttribute("logindto");
        // 로그인 세션에 저장되어 있는 세션을 이용해 memberNo 를 불러옵니다.
        int memberNo = loginDto.getMemberNo();
        // memberNo 에 해당하는 예약 내역을 불러옵니다.
        List<HistoryEntity> historyEntities = historyRepository.getHistoryByMemberNo(memberNo);
        model.addAttribute("histories", historyEntities);
        return "member/history_list";
    }

    // [내가 개설한 클래스와 맵핑]
    @GetMapping("/myclass")
    public String myclass(Model model, @PageableDefault Pageable pageable) {
        Page<RoomEntity> roomDtos = roomService.getroomlist(pageable);
        model.addAttribute("roomDtos", roomDtos);
        return "member/member_class";
    }

    // [내가 예약한 클래스 날짜에 대한 정보를 달력에 뿌려주기 위한 메소드]
    @GetMapping("/reservation")
    @ResponseBody
    public String reservationList() {
        HttpSession session = request.getSession();
        MemberDto loginDto = (MemberDto) session.getAttribute("logindto");
        // 로그인 세션에 저장되어 있는 세션을 이용해 memberNo 를 불러옵니다.
        int memberNo = loginDto.getMemberNo();
        // memberNo -> history -> timetable -> roomDate 순서로 접근해야한다.
        StringBuilder str = new StringBuilder();
        // 1. memberNo 사용해 History 엔티티를 List 형태로 호출한다.
        List<HistoryEntity> historyEntities = historyRepository.getHistoryByMemberNo(memberNo);
        // 2. history 엔티티와 맵핑되어있는 timetable 엔티티를 가져와서 roomDate 를 str 에 담는다.
        for (HistoryEntity history : historyEntities) {
            str.append(history.getTimeTableEntity().getRoomDate()).append(",");
            System.out.println(str);
        }
        return str.toString();
    }

    // [내가 예약한 RoomEntity 에 관한 정보를 캘린더에 뿌려주기 위한 메소드]
    @GetMapping("/memberHistoryJSON")
    @ResponseBody
    public JSONObject getRoomEntityByMemberNo(@RequestParam("date") String date) {

        HttpSession session = request.getSession();
        MemberDto loginDto = (MemberDto) session.getAttribute("logindto");
        int memberNo = loginDto.getMemberNo();

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        // memberNo 를 사용해서 History 엔티티를 불러옵니다.
        List<HistoryEntity> historyEntities = historyRepository.getHistoryByMemberNo(memberNo);
        // HistoryEntity 에는 memberEntity, roomEntity, timeTableEntity 가 모두 맵핑되어있습니다.

        TimeTableEntity timeTableEntity = null;
        RoomEntity roomEntity = null;

        // JSON 에 넘겨줄 정보는 다음과 같다
        // 1. title : 클래스 이름
        // 2. date : 클래스 개설 날짜
        // 3. beginTime, endTime : 클래스 시작 시간, 끝나는 시간
        // 4. category : 클래스 카테고리
        // 5. local : 클래스 지역
        // 6. address : 클래스 도로명 주소

        // History 엔티티를 for 문을 돌면서 JSON 에 저장시킨다.
        for (HistoryEntity historyEntity : historyEntities) {
            // for 문 안에서 data 에 정보를 누적시킵니다.
            JSONObject data = new JSONObject();

            // historyEntity 안에서 캘린더에서 클릭한 날짜와 동일한 정보만을 담아야합니다.
            if (historyEntity.getTimeTableEntity().getRoomDate().equals(date)) {
                if (timeTableRepository.findById(historyEntity.getTimeTableEntity().getTimeTableNo()).isPresent()) {
                    timeTableEntity = timeTableRepository.findById(historyEntity.getTimeTableEntity().getTimeTableNo()).get();
                    data.put("beginTime", timeTableEntity.getRoomTime().split(",")[0]);
                    data.put("endTime", timeTableEntity.getRoomTime().split(",")[1]);
                    data.put("date", timeTableEntity.getRoomDate());
                }
                if (roomRepository.findById(historyEntity.getRoomEntity().getRoomNo()).isPresent()) {
                    roomEntity = roomRepository.findById(historyEntity.getRoomEntity().getRoomNo()).get();
                    data.put("category", roomEntity.getRoomCategory());
                    data.put("local", roomEntity.getRoomLocal());
                    data.put("title", roomEntity.getRoomTitle());
                    data.put("address", roomEntity.getRoomAddress());
                    data.put("roomNo", roomEntity.getRoomNo());
                }
                jsonArray.add(data);
            }
        }
        jsonObject.put("json", jsonArray);
        return jsonObject;
    }

    // [메시지 페이지와 맵핑]
    @GetMapping("/msg")
    public String msg(Model model) {
        model.addAttribute("rooms", roomService.getmyroomlist());
        model.addAttribute("notes", roomService.getmynotelist());
        return "member/member_msg";
    }

    // [쪽지 쓰기]
    @GetMapping("/notereplywrite")
    @ResponseBody
    public String notereplywrite(@RequestParam("noteNo") int noteNo,
                                 @RequestParam("noteReply") String noteReply) {

        roomService.notereplywrite(noteNo, noteReply);

        return "1";
    }

    // [정산 페이지 맵핑]
    @GetMapping("/calculate")
    public String calculate() {
        return "member/calculate_page";
    }

    // 채널 맵핑
    @GetMapping("/channel")
    public String channel() {
        return "member/channel";
    }

    // 충전소 페이지 맵핑
    @GetMapping("/member_payment")
    public String payment(Model model) {
        // 1. 로그인 세션 호출
        HttpSession session = request.getSession();
        MemberDto loginDto = (MemberDto) session.getAttribute("logindto");
        MemberDto memberDto = memberService.getmemberDto(loginDto.getMemberNo());
        model.addAttribute("memberDto", memberDto);

        return "member/member_payment";
    }


    // 충전 처리 컨트롤러
    @GetMapping("/paymentcontroller")
    @ResponseBody
    public String paymentcontroller(@RequestParam("totalpay")int totalpay) {
        HttpSession session = request.getSession();
        MemberDto loginDto = (MemberDto) session.getAttribute("logindto");
        int memberNo = loginDto.getMemberNo();
        MemberEntity member = memberService.getMemberEntity(loginDto.getMemberNo());

        int memberPoint = member.getMemberPoint();

        boolean result = memberService.payment(memberNo, memberPoint,totalpay );
        if(result){
            return "1";
        } else{
            return "2";
        }


    }


}
