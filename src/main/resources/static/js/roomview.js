//
//$(function(){
//
//    var map = new kakao.maps.Map(document.getElementById('map'), { // 지도를 표시할 div
//        center : new kakao.maps.LatLng(36.2683, 127.6358), // 지도의 중심좌표
//        level : 14 // 지도의 확대 레벨
//    });
//
//    var clusterer = new kakao.maps.MarkerClusterer({
//        map: map, // 마커들을 클러스터로 관리하고 표시할 지도 객체
//        averageCenter: true, // 클러스터에 포함된 마커들의 평균 위치를 클러스터 마커 위치로 설정
//        minLevel: 10, // 클러스터 할 최소 지도 레벨
//        disableClickZoom: true // 클러스터 마커를 클릭했을 때 지도가 확대되지 않도록 설정한다
//    });
//
//    $("#classSearchBtn").on("click", function(){
//            // 검색어, 지역, 카테고리 를 인수로 넘겨받는다.
//            var keyword = $("#roomSearch").val();
//            var local = $("#classLocal").val();
//            var category = $("#classCategory").val();
//
//            $.ajax({
//                url : "/room/gongbang.json",
//                data : {"keyword" : keyword, "local" : local, "category" : category},
//                method : "GET",
//                success: function(data){
//                    var markers = $(data.positions).map(function(i, position) {
//                        var marker = new kakao.maps.Marker({ // return 막기
//                            position : new kakao.maps.LatLng(position.lat, position.lng)
//                        });
//
////                        kakao.maps.event.addListener(marker, 'click', function() {
////
////                              var customOverlay = new kakao.maps.CustomOverlay({
////                                 map: map,
////                                 // content: "<div style='padding:0 5px;background:#fff;'>"+position.roomNo+","+position.roomImg+"<br><a href='/room/view/"+position.roomNo+"'>바로가기</a></div>", // 내용물
////                                 position: new kakao.maps.LatLng(position.lat, position.lng) // 커스텀 오버레이를 표시할 좌표
////                                 // xAnchor: 0.5, // 컨텐츠의 x 위치
////                                 // yAnchor: 0 // 컨텐츠의 y 위치
////                              });
////                              alert(JSON.stringify(marker));
////                              return marker;
////                        });
//                        alert(JSON.stringify(marker));
//
//                    });
//                    clusterer.addMarkers(markers);
//                    kakao.maps.event.addListener(clusterer, 'clusterclick', function(cluster) {
//
//                    // 현재 지도 레벨에서 1레벨 확대한 레벨
//                    var level = map2.getLevel()-1;
//
//                    // 지도를 클릭된 클러스터의 마커의 위치를 기준으로 확대합니다
//                    map2.setLevel(level, {anchor: cluster.getCenter()});
//                    });
//                }
//            });
//
//
//        });
//});
//
//// 문의 버튼 클릭 이벤트
//function notewrite(roomNo){
//    var noteContents =$("#noteContents").val();
//    alert(roomNo);
//    alert(noteContents);
//    $.ajax({
//    url: '/room/notewrite',
//    data:{"roomNo":roomNo , "noteContents":noteContents},
//    success: function(data){
//        if(data==1){
//            alert("정상적으로 문의하셧습니다.");
//             $("#noteContents").val(""); // 내용물 초기화
//             $("#notemodal").modal("hide"); // 모달 종료
//        } else if(data==2){
//            alert("로그인 후 문의 가능합니다.");
//            // 로그인창 모달띄우기
//             $("#noteContents").val(""); // 내용물 초기화
//             $("#notemodal").modal("hide"); // 모달 종료
//
//            }
//        }
//
//    });
//}