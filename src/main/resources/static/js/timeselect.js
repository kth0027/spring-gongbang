    /*
        @Author : 김정진
        @Date : 2022-02-09
    */

    function inputCheck(){
        // 선택한 시간, 날짜 유효성 검사
        var beginTime = $("#beginTime").val();
        var endTime = $("#endTime").val();
        var date = $("#roomDate").val();

        if(date == ""){
            $("#dateMsg").html("날짜가 선택되지 않았습니다.");
            return false;
        } else {
            $("#dateMsg").html("");

        }

        if(beginTime == -1){
            $("#beginTimeMsg").html("시작 시간이 선택되지 않았습니다.");
            return false;
        } else {
            $("#beginTimeMsg").html("");
        }
        if(endTime == -1){
            $("#endTimeMsg").html("종료 시간이 선택되지 않았습니다.");
            return false;
        } else {
            $("#endTimeMsg").html("");
        }

        if(endTime < beginTime){
            $("#endTimeMsg").html("끝나는 시간이 시작 시간보다 빠를 수 없습니다.");
            return false;
        } else {
            $("#endTimeMsg").html("");
        }

    }
