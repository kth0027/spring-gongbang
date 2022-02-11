// 조지훈 02-08 클래스 신청
function classregistration(roomNo) {
    $.ajax({
        url: "/room/classregistration",
        data : {"roomNo" : roomNo},
        success: function(data) {
            if(data == 1) {
                   alert("등록 성공")
            }else {
                     alert("등록 실패")
            }
        }
    });
}