package com.ezen.controller;

import com.ezen.domain.dto.MemberDto;
import com.ezen.domain.entity.MemberEntity;
import com.ezen.domain.entity.ReplyEntity;
import com.ezen.domain.entity.RoomEntity;
import com.ezen.domain.entity.TimeTableEntity;
import com.ezen.domain.entity.repository.*;
import com.ezen.service.MemberService;
import com.ezen.service.ReplyService;
import com.ezen.service.RoomLikeService;
import com.ezen.service.RoomService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private RoomLikeService roomLikeService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private TimeTableRepository timeTableRepository;

    @Autowired
    ReplyService replyService;

    // [room_write.html 페이지와 맵핑]
    @GetMapping("/register")
    public String register() {
        return "room/room_register";
    }

    // [room_register_detail.html 페이지와 맵핑]
    @GetMapping("/registerDetail")
    public String registerDetail(Model model) {
        // member 정보를 가져와서 뿌려준다.
        HttpSession session = request.getSession();
        MemberDto loginDto = (MemberDto) session.getAttribute("logindto");
        MemberEntity memberEntity = memberService.getMember(loginDto.getMemberNo());
        model.addAttribute("member", memberEntity);
        return "room/room_register_detail";
    }

    // [클래스 개설하는 컨트롤러]
    // 클래스 만든 후에는 room_view.js.html 와 맵핑시킨다.
    @PostMapping("/registerClassController")
    public String registerClassController() {
        return "room/room_view";
    }

    /*
     * @Author : 김정진
     * @Date : 2022-02-07
     * 1. header 에 위치한 검색 창에서 '키워드검색' '지역 선택' '카테고리 선택' 세가지 경우에 결과값을 출력한다.
     * 2.
     * */

    // [개설된 강좌 출력]
    // 검색이 있는 경우 / 검색이 없는 경우 구분 짓는다.
    @GetMapping("/list")
    public String roomlist(@RequestParam("roomSearch") String keyword, @RequestParam("classLocal") String local, @RequestParam("classCategory") String category, Model model) {

        // 세션 호출
        HttpSession session = request.getSession();

        // 1. 검색, 지역, 카테고리 셋 중 하나라도 선택 했을 경우
        if (keyword != null || local != null || category != null) {
            session.setAttribute("keyword", keyword);
            session.setAttribute("local", local);
            session.setAttribute("category", category);
        }
        // 2. 아무것도 선택하지 않았을 경우, 이전 검색한 세션을 그대로 활용한다.
        else {
            keyword = (String) session.getAttribute("keyword");
            local = (String) session.getAttribute("local");
            category = (String) session.getAttribute("category");
        }

        List<RoomEntity> roomEntities = roomService.getRoomEntityBySearch(keyword, local, category);
        if (roomEntities != null) {
            model.addAttribute("roomEntities", roomEntities);
        } else {
            // 비정상적인 경로로 접근하면 error 페이지를 띄운다.
            return "error";
        }
        return "room/room_list";  // 타임리프를 통한 html 반환
    }

    // 메인 화면에서 지역 아이콘 선택했을 때 검색 후 결과 출력 페이지로 이동
    @GetMapping("/byLocal/{local}")
    public String roomListByLocal(@PathVariable("local") String local, Model model) {
        List<RoomEntity> roomEntities = roomService.getRoomEntityBySearch("", local, "");
        model.addAttribute("roomEntities", roomEntities);
        return "room/room_list";
    }

    // 메인 화면에서 카테고리 선택했을 때 검색 후 결과 출력 페이지로 이동
    @GetMapping("/byCategory/{category}")
    public String roomListByCategory(@PathVariable("category") String category, Model model) {
        List<RoomEntity> roomEntities = roomService.getRoomEntityBySearch("", "", category);
        model.addAttribute("roomEntities", roomEntities);
        return "room/room_list";
    }

    // 룸보기 페이지 이동
    @GetMapping("/view/{roomNo}") // 이동
    public String roomview(@PathVariable("roomNo") int roomNo, Model model) {

        // 1. 선택된 클래스 엔티티를 불러와서 Model 로 전달한다.
        RoomEntity roomEntity = roomService.getroom(roomNo);
        model.addAttribute("roomEntity", roomEntity);

        // 2. roomNo 이용해서 해당 강좌의 개설된 정보 (TimeTable) 을 불러온다.
        List<TimeTableEntity> timeTableEntities = roomEntity.getTimeTableEntity();
        model.addAttribute("timeTableEntities", timeTableEntities);
        return "room/room_view"; // 타임리프
    }

    // [ 작성한 클래스 등록 ]
    @PostMapping("/classRegister")
    @Transactional
    public String classRegister(RoomEntity roomEntity,
                                @RequestParam("roomImageInput") List<MultipartFile> files,
                                @RequestParam("addressX") double addressX,
                                @RequestParam("addressY") double addressY,
                                @RequestParam("checkBox1") String checkBox1,
                                @RequestParam("checkBox2") String checkBox2,
                                @RequestParam("checkBox3") String checkBox3) {
        // 1. roomStatus : 0 --> 검토중으로 설정
        roomEntity.setRoomStatus("검토중");
        roomEntity.setRoomETC(checkBox1 + "," + checkBox2 + "," + checkBox3);
        roomEntity.setRoomAddress(roomEntity.getRoomAddress() + "," + addressY + "," + addressX);
        roomService.registerClass(roomEntity, files);
        // 2. 등록 완료 후, 내가 등록한 클래스 페이지로 이동
        return "member/member_class";

    }

    // [ room_update.html 페이지와 맵핑 ]
    @GetMapping("/update/{roomNo}")
    public String update() {
        return "room/room_update";
    }

    // json 반환[지도에 띄우고자 하는 방 응답하기]
    @GetMapping("/gongbang.json")
    @ResponseBody
    public JSONObject gongbang() {
//    public JSONObject gongbang(@RequestParam("keyword") String keyword, @RequestParam("local") String local, @RequestParam("category") String category) {

        // 세션 호출
        HttpSession session = request.getSession();

        String keyword = (String) session.getAttribute("keyword");
        String local = (String) session.getAttribute("local");
        String category = (String) session.getAttribute("category");

        JSONObject jsonObject = new JSONObject(); // json 전체(응답용)
        JSONArray jsonArray = new JSONArray(); // json 안에 들어가   는 리스트
        List<RoomEntity> roomEntities = roomService.getRoomEntityBySearch(keyword, local, category);
        for (RoomEntity roomEntity : roomEntities) { //모든 방에서 하나씩 반복문 돌리기
            JSONObject data = new JSONObject(); // 리스트안에 들어가는 키:값 // 주소 =0 / 위도 =1 / 경도 =2

            data.put("lat", roomEntity.getRoomAddress().split(",")[1]); // 위도
            data.put("lng", roomEntity.getRoomAddress().split(",")[2]); // 경도
            data.put("roomTitle", roomEntity.getRoomTitle());
            data.put("roomNo", roomEntity.getRoomNo());
            data.put("roomImg", roomEntity.getRoomImgEntities().get(0).getRoomImg());
            jsonArray.add(data); //리스트에 저장
        }
        jsonObject.put("positions", jsonArray); // json 전체에 리스트 넣기
        return jsonObject;
    }


    @GetMapping("/addressXY")
    @ResponseBody
    public String addressXY(@RequestParam("roomNo") int roomNo) {
        return roomRepository.findById(roomNo).get().getRoomAddress();
    }


    // 내가 등록한 클래스 보기
    @GetMapping("/timeSelectPage/{roomNo}")
    public String timeSelectController(@PathVariable("roomNo") int roomNo, Model model) {
        // 1. 등록된 클래스 가져오기
        // List<RoomEntity> roomEntities = roomService.getmyroomlist();
        RoomEntity roomEntity = roomService.getroom(roomNo);
        model.addAttribute("room", roomEntity);
        return "member/member_timeselect";

    }

    // 등록한 클래스에 날짜, 시간 선택하기
    // /member/member_timeselect.html 에서 값을 받아옵니다.
    // form 태그로 받아오며 날짜, 시간, roomNo 를 받습니다.
    @GetMapping("/timeSelectController")
    public String timeSelectController(TimeTableEntity timeTableEntity,
                                       @RequestParam("beginTime") String beginTime,
                                       @RequestParam("endTime") String endTime,
                                       @RequestParam("roomNo") int roomNo,
                                       Model model, @PageableDefault Pageable pageable) {
        timeTableEntity.setRoomTime(beginTime + "," + endTime);

        boolean result = roomService.registerTimeToClass(timeTableEntity, roomNo);
        List<RoomEntity> roomDtos = roomService.getmyroomlist();
        model.addAttribute("roomDtos", roomDtos);
        return "member/member_class";
    }

    // @Author : 김정진
    // @Date : 2022-02-10
    // @Note : 특정 roomNo 에 해당하는 TimeTable 정보만 가져오는 메소드
    @GetMapping("/timetable")
    @ResponseBody
    public String getTimeTableByRoomNo(@RequestParam("roomNo") int roomNo) {
        StringBuilder str = new StringBuilder();
        List<TimeTableEntity> timeTableEntities = timeTableRepository.getTimeTableByRoomNo(roomNo);
        for (TimeTableEntity time : timeTableEntities) {
            str.append(time.getRoomDate()).append(",");
        }
        return str.toString();
    }

    // @Author: 김정진
    // @Date : 2022-02-10
    // @Note : YYYY-MM-DD 값으로 RoomEntity 를 조회 후 데이터 뿌려주기
    // JS 에서 Entity 를 읽을 수 없으니 JSON 형태로 변환해서 보낸다.
    @GetMapping("/toJSON")
    @ResponseBody
    public JSONObject getRoomEntityByTimeTableToJson(@RequestParam("activeId") String roomDate, @RequestParam("roomNo") int roomNo) {
        JSONObject jsonObject = new JSONObject(); // json
        JSONArray jsonArray = new JSONArray(); // json
        // roomNo 에 해당하는 TimeTable 엔티티만 리스트에 담아서 호출한다.
        List<TimeTableEntity> timeTableEntities = timeTableRepository.getTimeTableByRoomNo(roomNo);
        // roomNo 에 해당하는 개설된 강좌 전체를 for 문으로 조회한다.
        for (TimeTableEntity timeTableEntity : timeTableEntities) {
            RoomEntity roomEntity = roomRepository.findById(timeTableEntity.getRoomEntity().getRoomNo()).get();
            // 선택한 date 에 해당하는 Room 정보만을 json 에 저장시킨다.
            if (timeTableEntity.getRoomDate().equals(roomDate)) {
                JSONObject data = new JSONObject(); // json
                try {
                    data.put("roomNo", roomEntity.getRoomNo());
                    data.put("category", roomEntity.getRoomCategory());
                    data.put("title", roomEntity.getRoomTitle());
                    data.put("date", timeTableEntity.getRoomDate());
                    data.put("beginTime", timeTableEntity.getRoomTime().split(",")[0]);
                    data.put("endTime", timeTableEntity.getRoomTime().split(",")[1]);
                    data.put("local", roomEntity.getRoomLocal());
                    data.put("max", roomEntity.getRoomMax());

                    jsonArray.add(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        jsonObject.put("json", jsonArray);
        return jsonObject;
        // ajax 에서 받아온 stringify 된 데이터를 map 형태로 변환해서 model 에 출력한다.
        // @GetMapping("/")
    }

    // 문의 등록
    @GetMapping("/notewrite")
    @ResponseBody
    public String notewrite(@RequestParam("roomNo") int roomNo, @RequestParam("noteContents") String noteContents) {

        boolean result = roomService.notewrite(roomNo, noteContents);
        if (result) {
            return "1";
        } else {
            return "2";
        }
    }

    // 읽음처리 업데이트
    @GetMapping("/nreadupdate")
    @ResponseBody // 페이지 전환하면 안되서 사용
    public void nreadupdate(@RequestParam("noteNo") int noteNo) {

        roomService.nreadupdate(noteNo);
    }

    // [ review 페이지 맵핑 ] 01-27 조지훈
    @GetMapping("/review/{roomNo}")
    public String review(@PathVariable("roomNo") int roomNo, Model model) {
        model.addAttribute("roomNo", roomNo);
        return "room/room_review";
    }

}