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
    var date = year + "-" + (month + 1) + "-" + day; // 이 값이 개별 날짜 id
    $("#selectedDate").val(date);
    $("#roomDate").val(date);
    // 1. .active 인 객체를 클릭 시 ajax 를 한번 더 실행시켜서 해당 날짜에 개설된 강좌 정보를 뿌려야한다.

        $.ajax({
            url: "/room/toJSON", // RoomEntity 를 JSON 형태로 받아온다.
            data: {"activeId" : date, "roomNo" : roomNo},
            method: "GET",
            contentType: "application/json",
            success: function(data){
                // 받아온 정보들을 html 로 넘겨준다.
                // 반복문을 돌아야하니, 다시 controller 로 정보를 넘겨준 뒤
                // hashmap 형태로 받아서 model 로 넘겨준다.
                var rooms = $(data.json).map(function(i, room) {
                    console.log(data);
                    var roomNo = room.roomNo;
                    var roomCategory = room.category;
                    var roomTitle = room.title;
                    var roomTime = room.time;
                    var roomLocal = room.local;
                    var roomMax = room.max;

                    var roomhtml = "<div>";
                    roomhtml += "<div>" + roomNo + "</div>";
                    roomhtml += "<div>" + roomCategory + "</div>";
                    roomhtml += "<div>" + roomTitle + "</div>";
                    roomhtml += "<div>" + roomTime + "</div>";
                    roomhtml += "<div>" + roomLocal + "</div>";
                    roomhtml += "<div>" + roomMax + "</div>";
                    roomhtml += "</div>";

                    $("#time-select-inner").append(roomhtml);

                });
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
    var roomNo = $("#thisRoomNo").val();

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

        // 지난달
        for (var i = prevDate - prevDay + 1; i <= prevDate; i++) {
            calendar.innerHTML = calendar.innerHTML + '<div class="day prev disable day-select">' + i + '</div>';
        }

        var dataSplit = data.split(",");

        // 이번달
        for (var i = 1; i <= nextDate; i++) {
            // id : YYYY-MM-DD
            var dayId = currentYear + "-" + (currentMonth + 1) + "-" + i;
            var count = dataSplit.length;

             var j = 0;
            // while 문을 돌면서 개설된 날짜에 해당하는 div 에만 클릭 이벤트를 부여한다.
            while(j < count) {
                if(dayId == dataSplit[j]){
                    // TimeTable 에 저장된 roomDate 과 일치하는 경우만 출력된다.
                    calendar.innerHTML = calendar.innerHTML + '<div style="color: orange;" onclick="daySelect('+currentYear+','+currentMonth+','+i+','+roomNo+')" class="day current day-select active" id="'+dayId+'">' + i + '</div>';
                     j += 1;

                  break;
                } else {
                     j += 1;
                }
            }

            calendar.innerHTML = calendar.innerHTML + '<div style="color: gray;" class="day current day-select" id="'+dayId+'">' + i + '</div>';



            var test = $(".active");
        }



        // 다음달
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