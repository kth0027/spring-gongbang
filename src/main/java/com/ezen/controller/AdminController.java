package com.ezen.controller;

import com.ezen.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/roomlist")
    public String roomlist(Model model) {

        // List<Room> roomEntities = roomService.getroomlist();
        // model.addAttribute("roomEntities",roomEntities);

        return "admin/roomlist";
    }
}
