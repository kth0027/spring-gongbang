package com.ezen.service;

import com.ezen.domain.dto.MemberDto;
import com.ezen.domain.entity.BoardEntity;
import com.ezen.domain.entity.MemberEntity;
import com.ezen.domain.entity.PostEntity;
import com.ezen.domain.entity.PostReplyEntity;
import com.ezen.domain.entity.repository.PostReplyRepository;
import com.ezen.domain.entity.repository.PostRepository;
import com.ezen.domain.entity.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostReplyRepository postReplyRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    BoardService boardService;

    @Autowired
    MemberService memberService;

    @Autowired
    HttpServletRequest request;


    // 작성된 게시물 리스트 불러오기
    public List<PostEntity> getPostList(int boardNo) {
        return boardService.getPostList(boardNo);
    }

    // 작성된 게시물 엔티티 불러오기
    public PostEntity getPostEntity(int postNo) {
        Optional<PostEntity> postOptional = postRepository.findById(postNo);
        return postOptional.get();
    }

    // 게시글 등록하기
    @Transactional
    public boolean createPost(@Lazy PostEntity post, List<MultipartFile> files, int boardNo) {
        // 1. 로그인 세션 불러와서 회원 번호(memberNo) 변수에 초기화
        HttpSession session = request.getSession();
        MemberDto loginDTO = (MemberDto) session.getAttribute("loginSession");
        int memberNo = loginDTO.getMemberNo();
        // 1.1 로그인 된 회원 엔티티 호출하기
        MemberEntity memberEntity = memberService.getMemberEntity(loginDTO.getMemberNo());
        // 1.2 호출된 member 엔티티를 post 엔티티에 주입하기
        post.setMemberEntity(memberEntity);
        // 1.3 인수로 받아온 boardNo 로 board 엔티티 호출하기
        // 1.4 호출 한 board 엔티티를 post 에 주입하기
        BoardEntity board = boardService.getBoardEntity(boardNo);
        post.setBoardEntity(board);
        // 2. 부모 댓글이면 depth : 0
        // 2.1 자식 댓글이면 depth : 1
        // 3. 부모 댓글이면 order : 0
        // 3.1 자식 댓글이면 order : 1 부터 증가
        // 해당 컨트롤러에 접근한다는 것은 부모 게시글 이라는 뜻이므로 0 으로 set 한다.
        post.setPostDepth(0);
        post.setPostOrder(0);

        // 4. post 정보를 db 에 등록한 뒤 해당 post 를 엔티티로 호출한다.
        int postNo = postRepository.save(post).getPostNo();
        PostEntity savedPost = postRepository.findById(postNo).get();

        // 5. 저장된 post 객체를 member, board 엔티티에 저장한다.
        memberEntity.getPostEntities().add(savedPost);
        board.getPostList().add(post);

        return true;
    }

    // 게시물 삭제
    public boolean deletePost(int postNo) {
        Optional<PostEntity> post = postRepository.findById(postNo);
        if (post.isPresent()) {
            postRepository.delete(post.get()); // 게시물 삭제
            return true;
        } else {
            return false;
        }
    }

    // [댓글 등록]
    @Transactional
    public boolean replyWrite(String replyContent, @Lazy int postNo) {
        // 1. 로그인 세션을 호출해서 댓글 입력하는 회원 정보를 불러온다.
        HttpSession session = request.getSession();
        MemberDto loginDTO = (MemberDto) session.getAttribute("loginSession");
        // 2. 로그인 한 회원 엔티티는 member 객체에 담긴다.
        MemberEntity memberEntity = memberService.getMemberEntity(loginDTO.getMemberNo());
        // 2.1 작성하는 게시글 식별번호를 통해서 게시글 엔티티를 불러온다.
        PostEntity post = null;
        if (postRepository.findById(postNo).isPresent())
            post = postRepository.findById(postNo).get();
        // 3. Reply 엔티티를 builder 를 통해서 생성한다.
        // 3.1 부모 댓글은 order, depth 은 '0' 이고
        // 3.2 replyTarget 은 -1 값을 부여한다.
        PostReplyEntity postReplyEntity = PostReplyEntity.builder()
                .postReplyContent(replyContent)
                .postReplyOrder(0)
                .postReplyDepth(0)
                .postReplyTarget(-1)
                .build();
        // 3.3 reply 엔티티를 저장한다.
        // 3.4 저장된 reply 엔티티의 식별 번호를 변수에 할당한다.
        postReplyEntity.setMemberEntity(memberEntity);
        postReplyEntity.setPostEntity(post);

        int replyNo = postReplyRepository.save(postReplyEntity).getPostReplyNo();

        // 4. 저장된 reply 엔티티를 1 : N 관계로 맵핑되어있는 member, post 에 각각 저장한다.
        PostReplyEntity savedReply = postReplyRepository.findById(replyNo).get();
        // 5. 타겟 no 을 본인 번호로 업데이트 해준다.
        savedReply.setPostReplyTarget(savedReply.getPostReplyNo());
        memberEntity.getPostReplyEntities().add(savedReply);
        assert post != null;
        post.getPostReplyEntities().add(savedReply);
        return true;
    }

    // [대댓글 등록]
    @Transactional
    public boolean replyChildWrite(String content, int replyNo, int postNo) {
        // 1. 로그인 세션을 호출해서 댓글 입력하는 회원 정보를 불러온다.
        HttpSession session = request.getSession();
        MemberDto loginDTO = (MemberDto) session.getAttribute("loginSession");
        // 2. 로그인 한 회원 엔티티는 member 객체에 담긴다.
        MemberEntity member = memberService.getMember(loginDTO.getMemberNo());
        // 2.1 작성하는 게시글 식별번호를 통해서 게시글 엔티티를 불러온다.
        PostEntity postEntity = null;
        if (postRepository.findById(postNo).isPresent())
            postEntity = postRepository.findById(postNo).get();
        // 3. 대댓글을 등록한다.
        // 3.1 게시글 번호, 부모 댓글 번호, 작성 내용을 인수로 받는다.
        // 3.2 부모 댓글 depth=0 부터 시작해서 자식 댓글은 +1
        PostReplyEntity parentReply = null;
        if (postReplyRepository.findById(replyNo).isPresent())
            parentReply = postReplyRepository.findById(replyNo).get();
        assert parentReply != null;
        int parentReplyDepth = parentReply.getPostReplyDepth();
        int parentReplyOrder = parentReply.getPostReplyOrder();

        PostReplyEntity replyChild = PostReplyEntity.builder()
                .postReplyContent(content)
                .postReplyTarget(parentReply.getPostReplyNo())
                .build();
        // targetNo 에 해당하는 replyDepth 만을 선택한다.
        int depth = postReplyRepository.getReplyDepthByTargetNo(parentReply.getPostReplyNo());
        replyChild.setPostReplyDepth(depth);
        replyChild.setPostReplyOrder(depth);
        replyChild.setMemberEntity(member);
        replyChild.setPostEntity(postEntity);

        int replyChildNo = postReplyRepository.save(replyChild).getPostReplyNo();

        PostReplyEntity savedReplyChild = null;

        if (postReplyRepository.findById(replyChildNo).isPresent())
            savedReplyChild = postReplyRepository.findById(replyChildNo).get();
        member.getPostReplyEntities().add(savedReplyChild);
        assert postEntity != null;
        postEntity.getPostReplyEntities().add(savedReplyChild);
        return true;

    }

    // 등록된 '부모' 댓글 리스트 호출하기
    // 매끄러운 출력을 위해서 depth = 0 인 경우만 불러와야한다.
    public List<PostReplyEntity> getReplyList(int postNo) {
        return postReplyRepository.getParentReply();
    }

    // '자식' 댓글 리스트 호출하기
    // depth != 0 인 댓글을 모두 호출한다.
    public List<PostReplyEntity> getChildReplyList() {
        return postReplyRepository.getChildReply();
    }

}
