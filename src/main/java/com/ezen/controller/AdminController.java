package com.ezen.controller;

import com.ezen.domain.entity.RoomEntity;
import com.ezen.domain.entity.repository.RoomRepository;
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
    private HttpServletRequest request;

    @GetMapping("/adminTable")
    public String adminTable(Model model, @PageableDefault Pageable pageable){
        Page<RoomEntity> roomEntities = roomService.getroomlistadmin(pageable);
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

    public JSONObject adminGetAllRoom() {

        // RoomEntity 를 JSON 으로 변환 후 js 로 넘겨주는 역할
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        List<RoomEntity> roomEntities = roomRepository.findAll();

        // 반복문 돌면서 json 파일로 저장한다.
        for (RoomEntity roomEntity : roomEntities) {
            JSONObject data = new JSONObject();
            try {
                data.put("roomNo", roomEntity.getRoomNo());
                data.put("roomTitle", roomEntity.getRoomTitle());
                data.put("roomContent", roomEntity.getRoomContent());
                data.put("roomCategory", roomEntity.getRoomCategory());
                data.put("roomMax", roomEntity.getRoomMax());
                data.put("roomPrice", roomEntity.getRoomPrice());
                data.put("roomLocal", roomEntity.getRoomLocal());
                data.put("roomStatus", roomEntity.getRoomStatus());
                data.put("roomView", roomEntity.getRoomView());
                jsonArray.add(data);
            } catch (Exception e) {
            }
        }
        jsonObject.put("json", jsonArray);
        return jsonObject;

    }

    @GetMapping("/adminJSON")
    public JSONObject roomEntityToJSON(@RequestParam("keyword") String keyword,
                                       @RequestParam("local") String local,
                                       @RequestParam("category") String category,
                                       @PageableDefault Pageable pageable) {

        // 1. 검색 X 지역 X 카테고리 X 인 경우
        if (keyword.equals("-1") && category.equals("-1") && local.equals("-1")) {
            // 1. 전체 목록을 리턴하는 JSON 메소드 실행
            return adminGetAllRoom();
        }
        // 2. 검색, 지역, 카테고리 셋 중에 하나는 존재하는 경우
        else {
            // RoomEntity 를 JSON 으로 변환 후 js 로 넘겨주는 역할
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            Page<RoomEntity> roomEntities = adminService.adminGetRoomBySearch(keyword, local, category, pageable);

            // 반복문 돌면서 json 파일로 저장한다.
            for (RoomEntity roomEntity : roomEntities) {
                JSONObject data = new JSONObject();
                try {
                    data.put("roomNo", roomEntity.getRoomNo());
                    data.put("roomTitle", roomEntity.getRoomTitle());
                    data.put("roomContent", roomEntity.getRoomContent());
                    data.put("roomCategory", roomEntity.getRoomCategory());
                    data.put("roomMax", roomEntity.getRoomMax());
                    data.put("roomPrice", roomEntity.getRoomPrice());
                    data.put("roomLocal", roomEntity.getRoomLocal());
                    data.put("roomStatus", roomEntity.getRoomStatus());
                    data.put("roomView", roomEntity.getRoomView());
                    jsonArray.add(data);
                } catch (Exception e) {
                }
            }
            jsonObject.put("json", jsonArray);
            return jsonObject;
        }


    }

    public List<RoomEntity> adminListWithoutPageable(String keyword, String category, String local) {

        List<RoomEntity> roomEntities = null;

        return roomEntities;
    }

/*    @GetMapping("/adminListAfterSelect")
    @ResponseBody
    public Page<RoomEntity> adminListAfterSelect(Model model, @RequestParam("keyword") String keyword,
                                                 @RequestParam("category") String category,
                                                 @RequestParam("local") String local,
                                                 @PageableDefault Pageable pageable) {

        // 셋중에 하나는 null 이 아닌데, 나머지는 null 이나 "" 일 수 있으니 검사를 해야함
        // js 에서 그 검사를 하고 오지 않았기 때문에
        // 근데 그 작업을 header 에서 했던 것 처럼, 일단 service 로 넘기고
        // service 에서 처리한다.

        Page<RoomEntity> roomEntities = adminService.adminGetRoomBySearch(keyword, local, category, pageable);


        return roomEntities;
    }*/


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