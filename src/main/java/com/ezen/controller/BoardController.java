package com.ezen.controller;

import com.ezen.domain.entity.BoardEntity;
import com.ezen.domain.entity.CategoryEntity;
import com.ezen.domain.entity.PostEntity;
import com.ezen.service.BoardService;
import com.ezen.service.CategoryService;
import com.ezen.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    BoardService boardService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    PostService postService;

    // [게시판 페이지와 맵핑]
    // 카테고리, 게시판을 Model 에 담아서 넘겨준다.
    @GetMapping("/list")
    public String list(Model model) {

        // 1. 만들어진 카테고리 전체 호출
        List<CategoryEntity> categories = categoryService.getCategoryList();
        // 1. 카테고리에 해당하는 게시판 목록을 가져온다
        // 1.
        List<BoardEntity> boards = boardService.getBoards();
        // model 에 담아서 html 로 보낸다
        model.addAttribute("boards", boards);
        model.addAttribute("categories", categories);
        return "board/board_list";
    }

    // [게시판 생성 컨트롤러]
    @GetMapping("/newBoardController")
    @ResponseBody
    public String newBoardController(@RequestParam("boardName") String boardName, @RequestParam("categoryName") String categoryName) {
        boolean result = boardService.createNewBoard(boardName, categoryName);
        if (result) {
            return "1";
        } else {
            return "2";
        }
    }


}

