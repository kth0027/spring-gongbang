package com.ezen.controller;

import com.ezen.domain.entity.RoomEntity;
import com.ezen.service.RoomService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/room")
public class RoomController {
    @Autowired
    private RoomService roomService;

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

    @GetMapping("/list")
    public String roomlist(Model model) {
        List<RoomEntity> roomEntities = roomService.getroomlist();
        model.addAttribute("roomEntities",roomEntities);

        return "room/room_list";
    }

    // 룸보기 페이지ㅣ 이동
    @GetMapping("/view/{roomNo}") // 이동
    public String roomview(Model model){
        List<RoomEntity> roomEntities = roomService.getroomlist();
        model.addAttribute("roomEntities",roomEntities);

        return  "room/room_view"; // 타임리프
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

    // json 반환[지도에 띄우고자 하는 방 응답하기]
    @GetMapping("/gonbang.json")
    @ResponseBody
    public JSONObject gikbang(){
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


}
