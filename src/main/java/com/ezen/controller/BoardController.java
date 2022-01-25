package com.ezen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/board")
public class BoardController {

    // 클래스 리스트 이동
    @GetMapping("/list")
    public String boardlist() {
        return "board/board_list";
    }

    // view로 이동
    @GetMapping("/view")
    public String boardview() {
        return "board/board_view";
    }

    //update 이동
    @GetMapping("/update")
    public String boardupdate() {
        return "board/board_update";
    }

}

