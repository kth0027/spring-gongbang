$(function(){
    // room_list.html 이 실행될 때 마다 실행되는 함수
    var pageNo = $("#test").val();
    alert("현재 <" + pageNo + "> 번째 페이지를 보고 계십니다.");

    var map2 = new kakao.maps.Map(document.getElementById('map'), { // 지도를 표시할 div
       center : new kakao.maps.LatLng(36.2683, 127.6358), // 지도의 중심좌표
       level : 14 // 지도의 확대 레벨
    });

    var clusterer = new kakao.maps.MarkerClusterer({
       map: map2,
       averageCenter: true,
       minLevel: 10,
       disableClickZoom: true
    });

    $.ajax({
       url: "/room/gongbang.json",
       data: {"page" : pageNo},
       method: "GET",
       contentType: "application/json",
       success: function(data){
           alert(JSON.stringify(data));
           var markers = $(data.positions).map(function(i, position) {
               var marker = new kakao.maps.Marker({ // return 막기
                   position : new kakao.maps.LatLng(position.lat, position.lng)
               });
               kakao.maps.event.addListener(marker, 'click', function() {
                   // 커스텀 오버레이를 생성하고 지도에 표시한다
                   var customOverlay = new kakao.maps.CustomOverlay({
                       map: map2,
                       content: "<p> hi </p>",
                       position: new kakao.maps.LatLng(position.lat, position.lng), // 커스텀 오버레이를 표시할 좌표
                       xAnchor: 0.5, // 컨텐츠의 x 위치
                       yAnchor: 0 // 컨텐츠의 y 위치
                   });
               return marker;
               });
           return marker;
           });
           clusterer.addMarkers(markers);
           kakao.maps.event.addListener(clusterer, 'clusterclick', function(cluster) {
               var level = map2.getLevel()-1;
               map2.setLevel(level, {anchor: cluster.getCenter()});
           });
       }
    });
})

// room_list.html 에 지도를 뿌려주는 함수
// $.get 에서 $.ajax 로 변경
// 페이지 클릭 이벤트 발생 전


function isEmpty(str, defaultStr){
    if(typeof str == "undefined" || str == null || str == ""){
        str = defaultStr;
    }
    return str;
}


// 페이지 이동 후 맵 출력
//function getMapWithPage(page){
//
//    page = isEmpty(page, 0);
//
//    alert("페이지 클릭 후 클래스 리스트 출력합니다. ");
//
//    var map3 = new kakao.maps.Map(document.getElementById('map'), { // 지도를 표시할 div
//        center : new kakao.maps.LatLng(36.2683, 127.6358), // 지도의 중심좌표
//        level : 14 // 지도의 확대 레벨
//    });
//
//    var clusterer2 = new kakao.maps.MarkerClusterer({
//        map: map3,
//        averageCenter: true,
//        minLevel: 10,
//        disableClickZoom: true
//    });
//
//    // null 이나 undefined 일 때 0 값을 반환시킵니다.
//    page = isEmpty(page, 0);
//
//    alert("현재 보고 있는 페이지 번호 : " + page);
//
//    $.ajax({
//        url: "/room/gongbang2.json",
//        data: {"page" : page},
//        method: "GET",
//        contentType: "application/json",
//        async: false,
//        success: function(data){
//
//            var markers2 = $(data.positions).map(function(i, position) {
//                var marker2 = new kakao.maps.Marker({
//                    position : new kakao.maps.LatLng(position.lat, position.lng)
//                });
//                kakao.maps.event.addListener(marker2, 'click', function() {
//                    // 커스텀 오버레이를 생성하고 지도에 표시한다
//                    var customOverlay = new kakao.maps.CustomOverlay({
//                        map: map3,
//                        // content: "<div style='padding:0 5px;background:#fff;'>"+position.roomNo+","+position.roomImg+"<br><a href='/room/view/"+position.roomNo+"'>바로가기</a></div>", // 내용물
//                        content : "<p> hi </p>",
//                        position: new kakao.maps.LatLng(position.lat, position.lng),
//                        xAnchor: 0.5,
//                        yAnchor: 0
//                    });
//                return marker2;
//                });
//            return marker2;
//            });
//            clusterer2.addMarkers(markers2);
//            kakao.maps.event.addListener(clusterer2, 'clusterclick', function(cluster) {
//                var level = map3.getLevel()-1;
//                map3.setLevel(level, {anchor: cluster.getCenter()});
//            });
//
//
//        }
//    });
//}

// 문의 버튼 클릭 이벤트
function notewrite(roomNo){
   var noteContents =$("#noteContents").val();
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