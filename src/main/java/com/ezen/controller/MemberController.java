package com.ezen.controller;

import com.ezen.domain.dto.MemberDto;
import com.ezen.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequestMapping("/member")
@Controller
public class MemberController { // C S

    @Autowired
    MemberService memberService;

    @Autowired
    HttpServletRequest request; // 요청 객체    [ jsp : 내장객체(request)와 동일  ]

    // 회원가입페이지 연결
    @GetMapping("/signup")
    public String signup(){
        return "member/signup";
    }

    // 회원가입 처리 연결
    @PostMapping("/signupcontroller") // 회원가입 처리 연결
    public String signupcontroller(MemberDto memberDto
    ){
        memberService.membersignup(memberDto);
        return "redirect:/";  // 회원가입 성공시 메인페이지 연결
    }

    // 이메일 중복체크
    @GetMapping("/emailcheck")
    @ResponseBody
    public String emailcheck( @RequestParam("memail") String memail ){
        boolean result = memberService.emailcheck( memail);
        if( result ){
            return "1"; // 중복
        }else{
            return "2"; // 중복x
        }
    }

    // 로그인페이지 연결
    @GetMapping("/login")
    public String login(){
        return "member/login";
    }

    // 로그인처리
    @PostMapping("/logincontroller")
    @ResponseBody
    public String logincontroller( @RequestBody MemberDto memberDto ){

        MemberDto loginDto =   memberService.login( memberDto );
        if( loginDto !=null ){
            HttpSession session = request.getSession();   // 서버내 세션 가져오기
            session.setAttribute( "logindto" , loginDto );    // 세션 설정
            // session.getAttribute("logindto") ; // 세션 호출
            return "1";
        }else{
            return "2";
        }
        // 타임리프를 설치했을경우  RETRUN URL , HTML
        // html 혹은 url 아닌 값 반환할때  @ResponseBody
    }

    // 로그아웃 처리
    @GetMapping("/logout")
    public String logout(){
        HttpSession session = request.getSession();
        session.setAttribute( "logindto" , null);   // 기존 세션을 null 로 변경
        return "redirect:/"; // 로그아웃 성공시 메인페이지로 이동
    }

    // 마이페이지 연결
    @GetMapping("/info")
    public String info( Model model ){

        // 1. 로그인 세션 호출
        HttpSession session = request.getSession();
        MemberDto loginDto = (MemberDto) session.getAttribute("logindto");

        // 2. 세션에 회원정보를 service 에 전달해서 동일한 회원번호에 회원정보 가져오기
        MemberDto memberDto = memberService.getmemberDto(loginDto.getMnum());

        // 3. 찾은 회원정보를 model 인터페이스를 이용한 view 전달하기
        model.addAttribute( "memberDto", memberDto);

        return "member/info";
    }

    // 회원삭제 처리
    @GetMapping("/mdelete")
    @ResponseBody
    public int mdelete(
            @RequestParam("passwordconfirm") String passwordconfirm ){

        // 1. 세션 호출
        HttpSession session = request.getSession();
        MemberDto memberDto = (MemberDto) session.getAttribute("logindto");
        // 2. service에 로그인된 회원번호 , 확인패스워드
        boolean result =  memberService.delete( memberDto.getMnum() , passwordconfirm );
        // 3. 결과 를 ajax에게 응답
        if( result ){ return 1;}
        else{return 2;}
    }

    // 회원정보찾기 페이지로 연결
    @GetMapping("/findemail")
    public String findemail(){
        return "member/findemail";
    }

    // 이메일 찾기
    @PostMapping("/findemailcontroller")
    public String findemailcontroller(MemberDto memberDto , Model model){
        String result = memberService.findemail(memberDto);
        if( result != null ){
            String msg = " 회원님의 이메일 : " + result ;
            model.addAttribute("findemailmsg", msg);
        }else{
            String msg = " 동일한 회원정보가 없습니다." ;
            model.addAttribute("findemailmsg", msg);
        }

        return  "member/findemail";
    }

    // 비밀번호 찾기
    @PostMapping("/findpasswordcontroller")
    public String findpasswordcontroller( MemberDto memberDto , Model model ){
        String result = memberService.findpassword(memberDto);
        if( result != null ){
            String msg = " 회원님의 이메일 : " + result ;
            model.addAttribute("findpwmsg", msg);
        }else{
            String msg = " 동일한 회원정보가 없습니다." ;
            model.addAttribute("findpwmsg", msg);
        }
        return  "member/findemail";
    }


} // C E
