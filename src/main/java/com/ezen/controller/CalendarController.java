package com.ezen.controller;

import com.ezen.domain.dto.CalendarDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class CalendarController {
    /*
    * @Author : 김정진
    * @Date : 2022-02-07
    *
    * 캘린더를 호출할 페이지 :
    *   1. 클래스 상세 페이지 : 날짜 선택 후 해당 날짜에 등록된 클래스 시간 나타내기
    *   2. 클래스 날짜, 시간 등록 페이지 : 등록된 클래스의 날짜와 시간을 선택해서 강의를 등록한다.
    *   3. 예약 내역 : 캘린더의 날짜를 클릭하면, 해당 날짜에 수강했던 날짜만 클릭한다.
    *
    * 캘린더를 호출하면서 클래스 데이터가 등록되어 있는 날짜만 선택하게 한다.
    * 나머지 날짜들은 disabled 시켜서 클릭 못하게 막는다.
    *
    * [DB 와 연동]
    * 해당 페이지에 리스트를 출력하는 컨트롤러에 Model 에 같이 출력시킨다.
    * 캘린더 컨트롤러에서는 단순히 연, 월, 일을 출력한다.
    *
    *
    *
    *
    * */


    // 캘린더를 호출하면 해당 메서드가 실행된다.
    @RequestMapping(value = "calendar.do", method = RequestMethod.GET)
    public String getCalendar(Model model, HttpServletRequest request) {


        return "";

    }

    public void todayInfo() {
        // 캘린더 인스턴스 생성
        Calendar cal = Calendar.getInstance();
        // 현재 월의 시작 날짜
        int beginDay = cal.getMinimum(java.util.Calendar.DATE);
        // 현재 월의 종료 날짜
        int endDay = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
        // 현재 요일 [일요일 : 1, 토요일 : 7]
        int start = cal.get(java.util.Calendar.DAY_OF_WEEK);


    }



}
