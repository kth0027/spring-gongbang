$(function(){

    var map2 = new kakao.maps.Map(document.getElementById('map'), {
        center : new kakao.maps.LatLng(36.2683, 127.6358),
        level : 14
    });

    var clusterer = new kakao.maps.MarkerClusterer({
       map: map2,
       averageCenter: true,
       minLevel: 10,
       disableClickZoom: true
    });

    // room_list.html 이 실행될 때 마다 실행되는 함수
    var pageNo = $("#current-page").val();
    var keyword = $("#room-list-keyword").val();
    var category = $("#room-list-category").val();
    var local = $("#room-list-local").val();

    pageNo = isEmpty(pageNo, 0);
    keyword = isEmpty(keyword, -1);
    category = isEmpty(category, -1);
    local = isEmpty(local, -1);

    // 1. 검색이 있는 경우인지, 검색이 없는 경우인지를 판단하는 스위치가 필요합니다.
    // 2. 검색어, 지역 선택, 카테고리 선택 값을 받아와서 판단합니다.
    // 3. room_list.html 페이지 출력하는 컨트롤러에서 model 값으로 keyword, category, local 을 받아옵니다.
    // 4. 아무런 결과가 없다면, 전체 리스트를 출력해야합니다.

    // alert(keyword + "," + category + "," + local);
    // 5. 확인 결과 아무 값도 입력받지 않으면 "" -> 공백 값이 들어옵니다.
    if(keyword == -1 && category == -1 && local == -1){
        // 6. 아무 값도 없을 경우에는 기존에 있는 방식을 사용하지 않습니다.
            // 1. 리스트는 그대로 출력하되, JSON 을
        $.ajax({
            url: "/room/gongbangAll.json",
            method: "get",
            contentType: "application/json",
            success: function(data){
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
    } else {
        $.ajax({
            url: "/room/gongbang.json",
            data: {"page" : pageNo},
            method: "GET",
            contentType: "application/json",
            success: function(data){
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
    }


})

// undefined 인지, null 값인지, 공백인지를 체크하는 함수입니다.
function isEmpty(str, defaultStr){
    if(typeof str == "undefined" || str == null || str == ""){
        str = defaultStr;
    }
    return str;
}


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