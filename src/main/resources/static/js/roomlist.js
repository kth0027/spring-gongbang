
function rdelete(roomNo){
alert(roomNo);
    $.ajax({
        url:"/admin/delete",
        data:{"roomNo":roomNo},
        success:function(data){
            if(data==1){
                location.href ="/admin/adminlist";
            }

        }
    });

}


// 클래스 상태변경
function activeupdate(roomNo, active) {
    $.ajax({
            url:"/admin/activeupdate",
            data:{"roomNo":roomNo, "active":active},
            success:function(data){
                if(data==1){
                    location.href ="/admin/adminlist";
                } else {
                $("#activemsg").html("현재 동일한 상태입니다.");
                }

            }
        });
}