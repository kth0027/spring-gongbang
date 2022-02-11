
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

function activeupdate(roomNo, active) {
alert(roomNo);
alert(active);
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