package com.ezen;

import com.ezen.domain.entity.ReplyEntity;
import com.ezen.domain.entity.RoomEntity;
import com.ezen.service.ReplyService;
import com.ezen.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

    // 메인 페이지 맵핑
    // 메인 페이지 실행되면서 개설된 강의, 작성된 리뷰를 Model 로 뿌려줍니다.

    @GetMapping("/") // 최상위 경로
    public String index(Model model, @PageableDefault Pageable pageable) {

        // 개설된 강의 중 '승인완료' 처리된 강의만 출력된다.
        Page<RoomEntity> roomlist = roomService.getroomlist(pageable);
        model.addAttribute("roomlist", roomlist);
        // 댓글은 따로 상태가 없어서 모든 댓글이 출력된다.
        List<ReplyEntity> replylist = replyService.replylist();
        model.addAttribute("replylist", replylist);

        return "index";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    // 안읽은 쪽지의 갯수 세기
    @GetMapping("/nreadcount")
    @ResponseBody
    public void nreadcount() {
        roomService.nreadcount();
    }
}
