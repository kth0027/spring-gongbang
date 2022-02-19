package com.ezen.controller;

import com.ezen.domain.entity.BoardEntity;
import com.ezen.domain.entity.CategoryEntity;
import com.ezen.domain.entity.PostEntity;
import com.ezen.service.BoardService;
import com.ezen.service.CategoryService;
import com.ezen.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping(value = "/community")
public class CommunityController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BoardService boardService;

    @Autowired
    private PostService postService;

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

    // [특정 게시판 클릭 시 작성된 게시글을 리스트로 출력]
    @GetMapping("/postListController")
    public String postListController(Model model,
                                     @RequestParam("boardNo") int boardNo,
                                     @PageableDefault Pageable pageable){

        // 작성된 PostEntity 를 board_content.html 에 뿌려준다.
        Page<PostEntity> postEntities = postService.getPostList(boardNo, pageable);

        model.addAttribute("posts", postEntities);
        model.addAttribute("boardNo", boardNo);

        return "community/board_content";
    }

    // 게시물 작성하기 페이지 맵핑
    // 카테고리 -> 게시글 리스트 -> 게시글 작성
    // 등록 후 해당 게시판 리스트로 이동 (boardNo)
    @GetMapping("createPost")
    public String createPost(Model model,
                             @RequestParam("boardNo") int boardNo){

        model.addAttribute("boardNo", boardNo);

        return "community/create_post";

    }

    // 게시글 작성 컨트롤러
    @PostMapping("/createPostController")
    public String createPostController(Model model,
                                       @RequestParam("boardNo") int boardNo,
                                       @RequestParam("community-post-img-input") List<MultipartFile> files,
                                       @RequestParam("create-post-title") String title,
                                       @RequestParam("post-content") String content){

        PostEntity postEntity = PostEntity.builder()
                .postTitle(title)
                .postContent(content)
                .postViewCount(0)
                .build();

        boolean result = postService.createPost(postEntity, files, boardNo);

        return "";
    }



}
