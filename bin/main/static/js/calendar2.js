
/*
@Author : 김정진
@Date : 2022-02-09~
@Note :
    1. 인수를 전달받고 개설된 강좌만 선택되도록 하는 달력을 출력합니다.
*/

$(document).ready(function() {
    getTimeTable();
});

// 여기서 클릭 이벤트를 관리한다.
function daySelect(year , month , day, roomNo){
    // 선택한 날짜의 아이디 : YYYY-MM-DD
    var date = year + "-" + (month + 1) + "-" + day;
    // view 에 선택한 날짜를 텍스트로 보여줍니다.
    // 그리고 input 값으로 활용하기 위해서 하나는 hidden 으로 설정해두었습니다.
    $("#selectedDate").val(date);
    $("#roomDate").val(date);
    // 해당 날짜를 선택했을 때의 이벤트를 부여합니다.
    // DB 를 조회해서 선택한 날짜에 개설된 강좌 정보를 불러옵니다.
    $.ajax({
        url: "/room/toJSON", // RoomEntity 를 JSON 형태로 받아온다.
        data: {"activeId" : date, "roomNo" : roomNo},
        method: "GET",
        async: false,
        contentType: "application/json",
        success: function(data){
            $("#time-select-inner").empty();
            // 받아온 정보들을 html 로 넘겨준다.
            // 반복문을 돌아야하니, 다시 controller 로 정보를 넘겨준 뒤
            // hashmap 형태로 받아서 model 로 넘겨준다.
            var rooms = $(data.json).map(function(i, room) {

                var roomNo = room.roomNo;
                var roomCategory = room.category;
                var roomTitle = room.title;
                var roomBeginTime = room.beginTime;
                var roomEndTime = room.endTime;
                var roomLocal = room.local;
                var roomMax = room.max;
                var roomDate = room.date;

                var roomhtml = "<div class='col-sm-8'>";
                roomhtml += "<div class='classContent'>";
                roomhtml += "<div> 클래스 이름 : " + roomTitle + "</div>";
                roomhtml += "<div> 시작시간 : " + roomBeginTime + "</div>";
                roomhtml += "<div> 종료 : " + roomEndTime + "</div>";
                roomhtml += "<div> 지역 : " + roomLocal + "</div>";
                roomhtml += "<div> 최대 가능 인원 : " + roomMax + "</div>";
                roomhtml += "</div>";
                roomhtml += "</div>";
                roomhtml += "<div class='col-sm-4'>";
                roomhtml += "<button class='classBtn' onclick='registerClass("+roomNo+","+roomBeginTime+","+roomEndTime+","+roomDate+");'>";
                roomhtml += "클래스신청";
                roomhtml += "</button>";
                roomhtml += "</div>";

                $("#time-select-inner").append(roomhtml);
            });
        }
    });
}

// 특정 날짜, 특정 시간 클래스를 신청한다.
function registerClass(roomNo, beginTime, endTime, roomDate){
    var classTime = beginTime + "," + endTime;
    $.ajax({
        url: "/member/registerClass",
        data: {"roomNo" : roomNo, "classTime" : classTime, "roomDate" : roomDate},
        method: "GET",
        success: function(data){
            if(data==1){
                alert("성공");
            }
        }
    });
}

/*
    달력 렌더링 할 때 필요한 정보 목록
    현재 월(초기값 : 현재 시간)
    금월 마지막일 날짜와 요일
    전월 마지막일 날짜와 요일
*/

// DB 연동해서 데이터 가져오는 함수
function getTimeTable(){

    // @Param roomNo : 게시물 상세 페이지에 해당하는 클래스 번호
    let roomNo = $("#thisRoomNo").val();

    $.ajax({
        url: "/room/timetable",
        data: {"roomNo" : roomNo},
        method: "GET",
        success: function(data) {
            calendarInit(data, roomNo);
        }
    });
}

// 캘린더 출력하는 함수
function calendarInit(data, roomNo) {
    // 날짜 정보 가져오기
    var date = new Date(); // 현재 날짜(로컬 기준) 가져오기
    var utc = date.getTime() + (date.getTimezoneOffset() * 60 * 1000); // uct 표준시 도출
    var kstGap = 9 * 60 * 60 * 1000; // 한국 kst 기준시간 더하기
    var today = new Date(utc + kstGap); // 한국 시간으로 date 객체 만들기(오늘)

    var thisMonth = new Date(today.getFullYear(), today.getMonth(), today.getDate());
    // 달력에서 표기하는 날짜 객체

    var currentYear = thisMonth.getFullYear(); // 달력에서 표기하는 연
    var currentMonth = thisMonth.getMonth(); // 달력에서 표기하는 월
    var currentDate = thisMonth.getDate(); // 달력에서 표기하는 일

    // 캘린더 렌더링
    renderCalender(thisMonth);

    function renderCalender(thisMonth) {

        // 렌더링을 위한 데이터 정리
        currentYear = thisMonth.getFullYear();
        currentMonth = thisMonth.getMonth();
        currentDate = thisMonth.getDate();

        // 이전 달의 마지막 날 날짜와 요일 구하기
        var startDay = new Date(currentYear, currentMonth, 0);
        var prevDate = startDay.getDate();
        var prevDay = startDay.getDay();

        // 이번 달의 마지막날 날짜와 요일 구하기
        var endDay = new Date(currentYear, currentMonth + 1, 0);
        var nextDate = endDay.getDate();
        var nextDay = endDay.getDay();

        // console.log(prevDate, prevDay, nextDate, nextDay);

        // 현재 월 표기
        $('.year-month').text(currentYear + '.' + (currentMonth + 1));

        // 렌더링 html 요소 생성
        calendar = document.querySelector('.dates');
        calendar.innerHTML = '';

        // 지난달 달력 출력
        for (var i = prevDate - prevDay + 1; i <= prevDate; i++) {
            calendar.innerHTML = calendar.innerHTML + '<div class="day prev disable day-select">' + i + '</div>';
        }

        // DB 에 등록되어 있는 값들을 불러와서 ',' 로 split 한다.
        // [날짜1, 날짜2, ... , ] 식으로 불러오므로, 마지막에는 값이 없다.

        var dataSplit = data.split(",");

        // 이번달 달력 출력하는 반복문


        for (var i = 1; i <= nextDate; i++) {
            var flag = false;
            // id : YYYY-MM-DD
            let dayId = currentYear + "-" + (currentMonth + 1) + "-" + i;
            let count = dataSplit.length;
            // while 문 안에서 카운트
            // 강좌가 개설되었으면, j++ 시키며 다음 날짜부터 검색한다.
            let j = 0;
            while( j < count ){
                if(dayId == dataSplit[j]){
                    calendar.innerHTML = calendar.innerHTML + '<div style="color: orange;" onclick="daySelect('+currentYear+','+currentMonth+','+i+','+roomNo+')" class="day current day-select active" id="'+dayId+'">' + i + '</div>';
                    j = j + 1;
                    flag = true;
                    break;
                } else {

                    j = j + 1;
                }
            }
            if(flag == true){
            } else {
                calendar.innerHTML = calendar.innerHTML + '<div style="color: gray;" class="day current" id="'+dayId+'">' + i + '</div>';
            }


        }

        // 다음달 달력 출력
        for (var i = 1; i <= (7 - nextDay == 7 ? 0 : 7 - nextDay); i++) {
            calendar.innerHTML = calendar.innerHTML + '<div class="day next disable day-select">' + i + '</div>';
        }
        // 오늘 날짜 표기
        if (today.getMonth() == currentMonth) {
            todayDate = today.getDate();
            var currentMonthDate = document.querySelectorAll('.dates .current');
            currentMonthDate[todayDate -1].classList.add('today');
        }

    }

    // 이전달로 이동
    $('.go-prev').on('click', function() {
        thisMonth = new Date(currentYear, currentMonth - 1, 1);
        renderCalender(thisMonth);
        $(".dates").load(location.href + ".dates");
    });

    // 다음달로 이동
    $('.go-next').on('click', function() {
        thisMonth = new Date(currentYear, currentMonth + 1, 1);
        renderCalender(thisMonth);
        $(".dates").load(location.href + ".dates");
    });
}

