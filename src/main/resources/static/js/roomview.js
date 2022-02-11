$(function(){

    var map = new kakao.maps.Map(document.getElementById('map'), { // 지도를 표시할 div
        center : new kakao.maps.LatLng(36.2683, 127.6358), // 지도의 중심좌표
        level : 14 // 지도의 확대 레벨
    });

    var clusterer = new kakao.maps.MarkerClusterer({
        map: map, // 마커들을 클러스터로 관리하고 표시할 지도 객체
        averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
        minLevel: 10, // 클러스터 할 최소 지도 레벨
        disableClickZoom: true // 클러스터 마커를 클릭했을 때 지도가 확대되지 않도록 설정한다
    });

    $("#classSearchBtn").on("click", function(){

            // 검색어, 지역, 카테고리 를 인수로 넘겨받는다.
            var keyword = $("#roomSearch").val();
            var local = $("#classLocal").val();
            var category = $("#classCategory").val();

            alert(keyword, local, category);

            $.ajax({
                url : "/room/gongbang.json",
                data : {"keyword" : keyword, "local" : local, "category" : category},
                method : "GET",
                success: function(data){
                    alert(data);
                    var markers = $(data.positions).map(function(i, position) {
                        var marker = new kakao.maps.Marker({ // return 막기
                            position : new kakao.maps.LatLng(position.lat, position.lng)
                        });
                        alert(JSON.stringify(marker));
                    });
                    clusterer.addMarkers(markers);
                }
            });
        });
});

// 문의 버튼 클릭 이벤트
function notewrite(roomNo){
    var noteContents =$("#noteContents").val();
    alert(roomNo);
    alert(noteContents);
    $.ajax({
    url: '/room/notewrite',
    data:{"roomNo":roomNo , "noteContents":noteContents},
    success: function(data){
        if(data==1){
            alert("정상적으로 문의하셧습니다.");
             $("#noteContents").val(""); // 내용물 초기화
             $("#notemodal").modal("hide"); // 모달 종료
        } else if(data==2){
            alert("로그인 후 문의 가능합니다.");
            // 로그인창 모달띄우기
             $("#noteContents").val(""); // 내용물 초기화
             $("#notemodal").modal("hide"); // 모달 종료

            }
        }

    });
}