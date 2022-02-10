package com.ezen;

import com.ezen.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SpringBootApplication
@EnableJpaAuditing
public class AppStart {
    public static void main(String[] args) {
        SpringApplication.run(AppStart.class, args);
    }

    // 메인페이지 매핑[ 연결 ]
    @GetMapping("/") // 최상위 경로
    public String index() {
        return "index";
    }

//    @GetMapping("/error")
//    public String error() {
//        return "error";
//    }

    @Autowired
    private RoomService roomService;

    // 안읽은 쪽지의 갯수 세기
    @GetMapping("/nreadcount")
    @ResponseBody
    public void nreadcount(){
        roomService.nreadcount();
    }
}
