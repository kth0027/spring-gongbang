package com.ezen.controller;

import com.ezen.domain.entity.RoomEntity;
import com.ezen.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/room")
public class RoomController {

    @Autowired
    RoomService roomService;

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
    // 클래스 만든 후에는 room_view.html 와 맵핑시킨다.
    @PostMapping("/registerClassController")
    public String registerClassController() {
        return "room/room_view";
    }

    @GetMapping("/view")
    public String view() {
        return "room/room_view";
    }

    // [room_list.html 페이지와 맵핑]
    @GetMapping("/list")
    public String list() {
//        List<RoomEntity> roomEntityList = roomService.getRoomlist();
//        model.addAttribute("roomlist", roomEntityList);
        return "room/room_list";
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

    // [ room_update.html 페이지와 맵핑 ]
    @GetMapping("/update")
    public String update(){
        return "room/room_update";
    }

}
