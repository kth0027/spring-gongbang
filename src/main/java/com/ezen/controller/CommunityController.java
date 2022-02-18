package com.ezen.controller;

import com.ezen.domain.entity.BoardEntity;
import com.ezen.domain.entity.CategoryEntity;
import com.ezen.service.BoardService;
import com.ezen.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(value = "/community")
public class CommunityController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BoardService boardService;

    // 커뮤니티 탭 컨트롤러

    // [게시판 사이드 바 출력]
    // 카테고리, 게시판을 Model 에 담아서 넘겨준다.
    @GetMapping("/list")
    public String list(Model model) {
        // 1. 만들어진 카테고리 전체 호출
        List<CategoryEntity> categories = categoryService.getCategoryList();
        // 2. 카테고리에 해당하는 게시판 목록을 가져온다
        List<BoardEntity> boards = boardService.getBoards();
        // model 에 담아서 html 로 보낸다
        model.addAttribute("boards", boards);
        model.addAttribute("categories", categories);
        return "member/community";
    }

    // [특정 게시판 클릭 시 작성된 게시글 출력]
    @GetMapping("/postListController")
    public String postListController(Model model, @RequestParam("boardNo") int boardNo){



        return "community/board_content";
    }


}
