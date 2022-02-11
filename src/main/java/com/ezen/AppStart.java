package com.ezen;

import com.ezen.domain.entity.ReplyEntity;
import com.ezen.domain.entity.RoomEntity;
import com.ezen.service.ReplyService;
import com.ezen.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
@SpringBootApplication
@EnableJpaAuditing
public class AppStart {
    public static void main(String[] args) {
        SpringApplication.run(AppStart.class, args);
    }

    @Autowired
    ReplyService replyService;

    @Autowired
    RoomService roomService;
    // 메인페이지 매핑[ 연결 ]
    @GetMapping("/") // 최상위 경로
    public String index(Model model) {
        List<RoomEntity> roomlist = roomService.getroomlist();
        model.addAttribute("roomlist", roomlist);
        List<ReplyEntity> replylist = replyService.replylist();
        model.addAttribute("replylist", replylist);
        return "index";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
