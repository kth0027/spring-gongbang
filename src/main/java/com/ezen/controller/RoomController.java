package com.ezen.controller;

import com.ezen.domain.entity.RoomEntity;
import com.ezen.domain.entity.TimeTableEntity;
import com.ezen.domain.entity.repository.RoomRepository;
import com.ezen.service.RoomService;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;



import com.ezen.service.MemberService;
import com.ezen.service.RoomLikeService;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import java.util.List;

@Controller
@RequestMapping(value = "/room")
public class RoomController {

    @Autowired
    RoomService roomService;

    @Autowired
    MemberService memberService;

    @Autowired
    RoomLikeService roomLikeService;


    // [room_write.html 페이지와 맵핑]
    @GetMapping("/register")
    public String register() {
        return "room/room_register";
    }

    // [room_register_detail.html 페이지와 맵핑]
    @GetMapping("/registerDetail")
    public String registerDetail() {

        return "room/room_register_detail";
    }

    // [클래스 개설하는 컨트롤러]
    // 클래스 만든 후에는 room_view.js.html 와 맵핑시킨다.
    @PostMapping("/registerClassController")
    public String registerClassController() {

        return "room/room_view";
    }

    @Autowired
    HttpServletRequest request;


    @GetMapping("/list")
    public String roomlist(Model model) {


        List<RoomEntity> roomDtos = roomService.getroomlist();

        model.addAttribute("roomDtos", roomDtos);
        return "room/room_list";  // 타임리프 를 통한 html 반환
    }

    @GetMapping("/view/{roomNo}") // 이동
    public String roomview(@PathVariable("roomNo") int roomNo, Model model){
        RoomEntity roomEntity = roomService.getroom(roomNo);
        model.addAttribute("roomEntity",roomEntity);
        return "room/room_view"; // 타임리프
    }




    // [카테고리 선택 : 리스트 출력 컨트롤러]
    @GetMapping("/roomListCategoryController")
    public String roomListController(@PathVariable("roomCategory") String category, Model model) {
        // 1. DB 조회 후 room_list.html 에 Model 로 데이터 넘겨준다.
        return "room/room_list";
    }

    // [지역 선택 : 리스트 출력 컨트롤러]
    @GetMapping("/roomListAreaController")
    public String roomListAreaController(@PathVariable("roomListArea") String area, Model model) {
        return "room/room_list";
    }

    // [작성한 클래스 등록]
    @PostMapping("/classRegister")
    @Transactional
    public String classRegister(RoomEntity roomEntity,
                                @RequestParam("roomImageInput") List<MultipartFile> files,
                                @RequestParam("addressX") double addressX,
                                @RequestParam("addressY") double addressY,
                                @RequestParam("checkBox1") String checkBox1,
                                @RequestParam("checkBox2") String checkBox2,
                                @RequestParam("checkBox3") String checkBox3) {
        // 1. roomStatus : 0 --> 승인 대기중으로 설정
        roomEntity.setRoomStatus(0);
        roomEntity.setRoomETC(checkBox1 + "," + checkBox2 + "," + checkBox3);
        roomEntity.setRoomAddress(roomEntity.getRoomAddress() + "," + addressY + "," + addressX);
        boolean result = roomService.registerClass(roomEntity, files);

        return "member/member_class";
    }


    // [ room_update.html 페이지와 맵핑 ]
    @GetMapping("/update/{roomNo}")
    public String update(){
        return "room/room_update";
    }

    // json 반환[지도에 띄우고자 하는 방 응답하기]
    @GetMapping("/gongbang.json")
    @ResponseBody
    public JSONObject gongbang(){
        // Map <--> Json[키:값] => 엔트리
        // {"키": 리스트{ "키" : 값1, "키" : 값2}} => 중첩 가능
        // map ={키:값}
        // map 객체 = {"키":List[map 객체, map 객체]}
        JSONObject jsonObject = new JSONObject(); // json 전체(응답용)
        JSONArray jsonArray = new JSONArray(); // json 안에 들어가는 리스트


        List<RoomEntity> roomlist = roomService.getroomlist(); // 모든 방[위도, 경도 포함]
        for(RoomEntity roomEntity : roomlist){ //모든 방에서 하나씩 반복문 돌리기
            JSONObject data = new JSONObject(); // 리스트안에 들어가는 키:값 // 주소 =0 / 위도 =1 / 경도 =2


            data.put("lat", roomEntity.getRoomAddress().split(",")[0]); // 위도
            data.put("lng", roomEntity.getRoomAddress().split(",")[1]); // 경도
            data.put("roomTitle",roomEntity.getRoomTitle());
            data.put("roomNo", roomEntity.getRoomNo());
            //data.put("rImg", roomEntity.getRoomImgEntities().get(0).getRImg());


            jsonArray.add(data); //리스트에 저장

        }

        jsonObject.put("positions", jsonArray); // json 전체에 리스트 넣기


        return jsonObject;
    }


    @GetMapping("/addressXY")
    @ResponseBody
    public String addressXY(@RequestParam("roomNo") int roomNo){

        return roomRepository.findById( roomNo ).get().getRoomAddress();
    }

    @Autowired
    private RoomRepository roomRepository;


    @GetMapping("/room_pay")
    public String room_pay() {
        return "room/room_pay";
    }


    @GetMapping("/room/roompayment")
    public String roompayment() {
        return "room/roompayment";
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
    @GetMapping("/timeSelectController")
    public String timeSelectController(TimeTableEntity timeTableEntity,
                                       @RequestParam("beginTime") String beginTime,
                                       @RequestParam("endTime") String endTime,
                                       @RequestParam("roomNo") int roomNo,
                                       Model model, @PageableDefault Pageable pageable) {
        timeTableEntity.setRoomTime(beginTime + "," + endTime);

        boolean result = roomService.registerTimeToClass(timeTableEntity, roomNo);
        Page<RoomEntity> roomDtos = roomService.getmyroomlist(pageable);
        model.addAttribute("roomDtos", roomDtos);
        return "member/member_class";

    }

}
