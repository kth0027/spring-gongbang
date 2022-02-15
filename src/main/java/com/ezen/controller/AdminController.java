package com.ezen.controller;

import com.ezen.domain.entity.RoomEntity;
import com.ezen.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/adminlist")
    public String adminlist(Model model, @PageableDefault Pageable pageable) {
        Page<RoomEntity> roomEntities = roomService.getroomlist(pageable);
        model.addAttribute("roomEntities", roomEntities);
        return "admin/adminlist";
    }


    @GetMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("roomNo") int roomNo) {
        roomService.delete(roomNo);
        return "1";
    }

    // 방번호를 이용한 방 상태변경
    // '검토중' '승인중' '승인완료' '승인거부' '정원마감'
    @GetMapping("/activeupdate")
    @ResponseBody
    public String activeupdate(@RequestParam("roomNo") int roomNo,
                               @RequestParam("active") String update) {
        boolean result = roomService.activeupdate(roomNo, update);
        if (result) {
            return "1";
        } else {
            return "2";
        }


    }


}
