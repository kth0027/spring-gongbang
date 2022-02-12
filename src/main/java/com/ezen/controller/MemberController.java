package com.ezen.controller;

import com.ezen.domain.dto.MemberDto;
import com.ezen.domain.entity.HistoryEntity;
import com.ezen.domain.entity.MemberEntity;
import com.ezen.domain.entity.RoomEntity;
import com.ezen.domain.entity.TimeTableEntity;
import com.ezen.domain.entity.repository.*;
import com.ezen.service.MemberService;
import com.ezen.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RequestMapping("/member")
@Controller
public class MemberController { // C S

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HttpServletRequest request; // 요청 객체    [ jsp : 내장객체(request)와 동일  ]

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

    // 회원가입페이지 연결
    @GetMapping("/signup")
    public String signup() {
        return "member/signup";
    }

    // 회원가입 처리 연결
    @PostMapping("/signupcontroller") // 회원가입 처리 연결
    public String signupcontroller(MemberDto memberDto
    ) {
        memberService.membersignup(memberDto);
        return "redirect:/";  // 회원가입 성공시 메인페이지 연결
    }

    // 이메일 중복체크
    @GetMapping("/emailcheck")
    @ResponseBody
    public String emailcheck(@RequestParam("memail") String memail) {
        boolean result = memberService.emailcheck(memail);
        if (result) {
            return "1"; // 중복
        } else {
            return "2"; // 중복x
        }
    }

    // 로그인페이지 연결
    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

    // 로그인처리
//    @PostMapping("/logincontroller")
//    @ResponseBody
//    public String logincontroller(@RequestBody MemberDto memberDto) {
//
//        MemberDto loginDto = memberService.login(memberDto);
//        if (loginDto != null) {
//            HttpSession session = request.getSession();   // 서버내 세션 가져오기
//            session.setAttribute("logindto", loginDto);    // 세션 설정
//            // session.getAttribute("logindto") ; // 세션 호출
//            return "1";
//        } else {
//            return "2";
//        }
//        // 타임리프를 설치했을경우  RETRUN URL , HTML
//        // html 혹은 url 아닌 값 반환할때  @ResponseBody
//    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout() {
        HttpSession session = request.getSession();
        session.setAttribute("logindto", null);   // 기존 세션을 null 로 변경
        return "redirect:/"; // 로그아웃 성공시 메인페이지로 이동
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
    public String registerClass(@RequestParam("roomNo") int roomNo,
                                @RequestParam("classTime") String classTime,
                                @RequestParam("roomDate") String roomDate) {

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
        // 2. 받아온 시간으로 TimeTable 을 가져온다.
        // 2.1 TimeTable 내에서 roomTime 에 해당하는 것만 등록한다.
        List<TimeTableEntity> timeTableEntities = timeTableRepository.getTimeTableByRoomNo(roomNo);
        for (TimeTableEntity timeTableEntity : timeTableEntities) {
            if (timeTableEntity.equals(classTime)) {
                timeTableTmp = timeTableEntity;
            }
        }
        // 3. HistoryEntity 에 멤버 정보, 클래스 정보를 들록합니다.
        HistoryEntity historyEntity = HistoryEntity.builder()
                .memberEntity(memberEntity)
                .roomEntity(roomEntity)
                .build();

        // 4. 예약내역 저장하고 저장번호 받아오기
        int savedHistoryEntityNo = historyRepository.save(historyEntity).getHistoryNo();

        // 5. 위에서 저장한 예약내역 가져오기
        HistoryEntity savedHistoryEntity = historyRepository.findById(savedHistoryEntityNo).get();

        memberEntity.getHistoryEntities().add(savedHistoryEntity);
        assert roomEntity != null;
        roomEntity.getHistoryEntities().add(savedHistoryEntity);

        return "1";
    }


    // [예약내역(히스토리) 페이지와 맵핑]
    @GetMapping("/history")
    public String history() {
        return "member/history_list";
    }

    // [내가 개설한 클래스와 맵핑]
    @GetMapping("/myclass")
    public String myclass(Model model) {


        List<RoomEntity> roomDtos = roomService.getroomlist();

        model.addAttribute("roomDtos", roomDtos);

        return "member/member_class";
    }

    // [메시지 페이지와 맵핑]
    @GetMapping("/msg")
    public String msg(Model model) {
        model.addAttribute("rooms", roomService.getmyroomlist());
        model.addAttribute("notes", roomService.getmynotelist());
        return "member/member_msg";
    }

    // 쪽지 쓰기
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


}
