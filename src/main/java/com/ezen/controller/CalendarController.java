package com.ezen.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Calendar;

@Controller
@RequestMapping(value = "calendar.do", method = RequestMethod.GET)
public class CalendarController {

    Calendar cal = Calendar.getInstance();
    DateData calednarData;

}
