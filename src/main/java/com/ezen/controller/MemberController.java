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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.File;
import java.util.List;
import java.util.UUID;

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

        // 5. 위에서 저장한 예약내역 가져오기
        HistoryEntity savedHistoryEntity = historyRepository.findById(savedHistoryEntityNo).get();

        // 6. historyEntity 를 TimeTable Entity 에 선언한 List<HistoryEntity> 에 추가한다.
        assert timeTableTmp != null;
        timeTableTmp.getHistoryEntity().add(savedHistoryEntity);

        memberEntity.getHistoryEntities().add(savedHistoryEntity);

        assert roomEntity != null;
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

    // [내가 예약한 클래스와 맵핑]
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
        for(HistoryEntity history : historyEntities){
            str.append(history.getTimeTableEntity().getRoomDate()).append(",");
            System.out.println(str);
        }
        return str.toString();
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

    
    @GetMapping("/channel/{memberNo}")
    public String channel(@PathVariable("memberNo") int memberNo, Model model) {
        // 02-15 채널 정보 출력 - 조지훈
        MemberEntity memberEntity = memberService.getMember(memberNo);

        model.addAttribute("memberEntity", memberEntity);
        return "member/channel";
    }

    // 02-15 채널 정보 등록 페이지 맵핑 - 조지훈
//    @GetMapping("/channelupdate/{memberNo}")
//    public String channelupdate(@PathVariable("memberNo") int memberNo, Model model){
//        model.addAttribute("memberNo", memberNo);
//        return "member/channelupdate";
//    }

    // 02-15 채널 정보 등록  - 조지훈
    @PostMapping("/channelupdatecontroller")
    public String channelupdatecontroller(@RequestParam("memberNo") int memberNo,
                                          @RequestParam("channelContent") String channelContent,
                                          @RequestParam("channelTitle") String channelTitle,
                                          @RequestParam("memberImg") MultipartFile file) {
        try {
            UUID uuid = UUID.randomUUID();
            String uuidfile = uuid.toString() + "_" + file.getOriginalFilename().replaceAll("_", "-");
            String dir = "C:\\Users\\505\\Desktop\\gongbang\\src\\main\\resources\\static\\channelimg";
            String filepath = dir + "\\" + uuidfile;
            file.transferTo(new File(filepath));
            memberService.channelupdate(
                    MemberEntity.builder().memberNo(memberNo).channelTitle(channelTitle).channelContent(channelContent).channelImg(uuidfile).build());
        } catch (Exception e) {
            System.out.println(e);
        }
        return "redirect:/member/channel/"+memberNo;
    }

}
