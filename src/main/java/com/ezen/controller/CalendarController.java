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

    @RequestMapping(value = "calendar.do", method = RequestMethod.GET)
    public String getCalendaer(Model model, HttpServletRequest request, CalendarDTO calendarDTO) {
        Calendar cal = Calendar.getInstance();
        CalendarDTO calendarData;

        if (calendarDTO.getDate().equals("") && calendarDTO.getMonth().equals("")) {
            calendarDTO = new CalendarDTO(String.valueOf(cal.get(Calendar.YEAR)), String.valueOf(cal.get(Calendar.MONTH)), String.valueOf(cal.get(Calendar.DATE)), null);
        }

        Map<String, Integer> todayInfo = todayInfo(calendarDTO);
        List<CalendarDTO> dateList = new ArrayList<CalendarDTO>();

        for (int i = 1; i < todayInfo.get("start"); i++) {
            calendarData = new CalendarDTO(null, null, null, null);
            dateList.add(calendarData);
        }

        for (int i = todayInfo.get("startDay"); i <= todayInfo.get("endDay"); i++) {
            if (i == todayInfo.get("today")) { //
                calendarData = new CalendarDTO(String.valueOf(calendarDTO.getYear()), String.valueOf(calendarDTO.getMonth()), String.valueOf(i), null);
            }
        }

        return null;


    }

    public Map<String, Integer> todayInfo(CalendarDTO calendarDTO) {
        Map<String, Integer> todayData = new HashMap<String, Integer>();
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(calendarDTO.getYear()), Integer.parseInt(calendarDTO.getMonth()), 1);

        // 현재 월의 시작 날짜
        int beginDay = cal.getMinimum(java.util.Calendar.DATE);
        // 현재 월의 종료 날짜
        int endDay = cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
        // 현재 요일 [일요일 : 1, 토요일 : 7]
        int start = cal.get(java.util.Calendar.DAY_OF_WEEK);

        Calendar todayCal = Calendar.getInstance();
        SimpleDateFormat ysdf = new SimpleDateFormat("yyyy");
        SimpleDateFormat msdf = new SimpleDateFormat("M");

        int todayYear = Integer.parseInt(ysdf.format(todayCal.getTime()));
        int todayMonth = Integer.parseInt(msdf.format(todayCal.getTime()));

        int searchYear = Integer.parseInt(calendarDTO.getYear());
        int searchMonth = Integer.parseInt(calendarDTO.getMonth()) + 1;

        int today = -1;
        if (todayYear == searchYear && todayMonth == searchMonth) {
            SimpleDateFormat dsdf = new SimpleDateFormat("dd");
            today = Integer.parseInt(dsdf.format(todayCal.getTime()));
        }

        searchMonth = searchMonth - 1;

        Map<String, Integer> beforeAfterCalendar = beforeAfterCalendar(searchYear, searchMonth);

        todayData.put("start", start);
        todayData.put("beginDay", beginDay);
        todayData.put("endDay", endDay);
        todayData.put("today", today);
        todayData.put("searchYear", searchYear);
        todayData.put("searchMonth", searchMonth + 1);
        todayData.put("beforeYear", beforeAfterCalendar.get("beforeYear"));
        todayData.put("beforeMonth", beforeAfterCalendar.get("beforeMonth"));
        todayData.put("afterYear", beforeAfterCalendar.get("afterYear"));
        todayData.put("afterMonth", beforeAfterCalendar.get("afterMonth"));
        return todayData;

    }

    private Map<String, Integer> beforeAfterCalendar(int searchYear, int searchMonth) {
        Map<String, Integer> beforeAfterData = new HashMap<String, Integer>();
        int beforeYear = searchYear;
        int beforeMonth = searchMonth - 1;
        int afterYear = searchYear;
        int afterMonth = searchMonth + 1;

        if (beforeMonth < 0) {
            beforeMonth = 11;
            beforeYear = searchYear - 1;
        }
        if (searchMonth > 11) {
            afterMonth = 0;
            afterYear = searchYear + 1;
        }

        beforeAfterData.put("beforeYear", beforeYear);
        beforeAfterData.put("beforeMonth", beforeMonth);
        beforeAfterData.put("afterYear", afterYear);
        beforeAfterData.put("afterMonth", afterMonth);

        return beforeAfterData;

    }


}
