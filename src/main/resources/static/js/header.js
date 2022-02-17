function channelcheck(memberNo) {

    $.ajax({
        url: "/member/channelcheck",
        data: {"memberNo" : memberNo},
        success: function(data){
            if(data == 1) {
                alert("강사소개 작성 후 클래스 개설이 가능합니다.");
                location.href="/member/channel/"+memberNo;
            }
        }

    });
}