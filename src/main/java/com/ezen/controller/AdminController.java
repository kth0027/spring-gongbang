package com.ezen.controller;

import com.ezen.domain.entity.HistoryEntity;
import com.ezen.domain.entity.RoomEntity;
import com.ezen.domain.entity.TimeTableEntity;
import com.ezen.domain.entity.repository.HistoryRepository;
import com.ezen.domain.entity.repository.RoomRepository;
import com.ezen.domain.entity.repository.TimeTableRepository;
import com.ezen.service.AdminService;
import com.ezen.service.RoomService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private TimeTableRepository timeTableRepository;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/adminTableAll")
    public String adminTable(Model model, @PageableDefault Pageable pageable) {
        Page<RoomEntity> roomEntities = roomService.getroomlistadmin(pageable);
        model.addAttribute("roomEntities", roomEntities);
        return "admin/admin_table";
    }

    @GetMapping("/adminTableBySearch")
    public String adminTableBySearch(Model model,
                                     @PageableDefault Pageable pageable,
                                     @RequestParam("keyword") String keyword,
                                     @RequestParam("category") String category,
                                     @RequestParam("local") String local) {
        // 1. 각 입력값의 존재 여부는 RoomService 에서 확인한다.
        Page<RoomEntity> roomEntities = roomService.adminGetRoomEntityBySearch(pageable, keyword, local, category);
        // 2. 조회한 값을 Model 을 통해 table 형태로 출력한다.
        model.addAttribute("roomEntities", roomEntities);
        return "admin/admin_table";
    }


    @GetMapping("/adminlist")
    public String adminlist(Model model, @PageableDefault Pageable pageable) {
        // 1. 아무 검색이 없는 경우 [초기 진입 화면]
        // 1.1 header.html href 통해서 들어온다.
        // 별다른 조건없이 모든 데이터 뿌려준다.
        Page<RoomEntity> roomEntities = roomService.getroomlistadmin(pageable);
        model.addAttribute("roomEntities", roomEntities);
        return "admin/adminlist";
    }

    @GetMapping("/roomJSON")
    @ResponseBody
    public JSONObject roomToJSON(@RequestParam("year") int year,
                                 @RequestParam("month") int month,
                                 @RequestParam("day") int day,
                                 @RequestParam("category") String category,
                                 @RequestParam("local") String local) {

        // RoomEntity 를 JSON 으로 변환 후 js 로 넘겨주는 역할
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        // 1. historyEntities 는 전체 회원이 예약한 내역이다.
        List<HistoryEntity> historyEntities = historyRepository.findAll();
        // 2. 예약 내역에서 각각 RoomEntity, TimeTableEntity 에 접근해서
        // 1. RoomEntity
            // 1. roomCategory
            // 2. roomLocal
        // 2. TimeTableEntity
            // 1. roomDate
            // 2. roomTime
        // 3. HistoryEntity
            // 1. historyPoint
            // 2. createdDate
        for (HistoryEntity historyEntity : historyEntities) {
            TimeTableEntity timeTableEntity = null;
            RoomEntity roomEntity = null;
            // 1. 데이터를 담을 JSONObject 생성
            JSONObject data = new JSONObject();
            // 2. 현재 예약건에 해당하는 강좌 시간 정보
            int timeTableNo = historyEntity.getTimeTableEntity().getTimeTableNo();
            if (timeTableRepository.findById(timeTableNo).isPresent()) {
                timeTableEntity = timeTableRepository.findById(timeTableNo).get();
            }
            assert timeTableEntity != null;
            data.put("date", timeTableEntity.getRoomDate()); // YYYY-MM-DD
            data.put("beginTime", timeTableEntity.getRoomTime().split(",")[0]); // HH, HH
            data.put("endTime", timeTableEntity.getRoomTime().split(",")[1]); // HH, HH

            // 3. 현재 예약건에 해당하는 강좌 정보
            int roomNo = historyEntity.getRoomEntity().getRoomNo();
            if (roomRepository.findById(roomNo).isPresent()) {
                roomEntity = roomRepository.findById(roomNo).get();
            }
            assert roomEntity != null;
            data.put("category", roomEntity.getRoomCategory());
            data.put("local", roomEntity.getRoomLocal());

            // 4. 예약 정보
            data.put("createdDate", historyEntity.getCreatedDate()); // 예약이 완료된 날짜
            data.put("price", historyEntity.getHistoryPoint()); // 회원이 결제한 금액

            jsonArray.add(data);
        }
        jsonObject.put("history", jsonArray);
        return jsonObject;
    }


    public List<RoomEntity> adminListWithoutPageable(String keyword, String category, String local) {
        List<RoomEntity> roomEntities = null;
        return roomEntities;
    }

    @GetMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam("roomNo") int roomNo) {
        roomService.delete(roomNo);
        return "1";
    }

    // 방번호를 이용한 방 상태변경
    // '검토중' '승인중' '승인완료' '승인거부' '정원마감'
    @GetMapping("/activeupdate")
    @ResponseBody
    public String activeupdate(@RequestParam("roomNo") int roomNo,
                               @RequestParam("active") String update) {
        boolean result = roomService.activeupdate(roomNo, update);
        if (result) {
            return "1";
        } else {
            return "2";
        }
    }

}