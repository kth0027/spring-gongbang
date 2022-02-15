function roomnavy(roomNo){
$("#modalnavy1").trigger('click');

$("#road").val( roomNo );

initTmap();
/*    $.ajax({
        url:"/room/roomnavy",
        method : "POST",
        success:function(data){
            $("#modalnavy1").trigger('click');
            $("#modalnavy2").html( data );
        }
    });*/

}

var map;
var markerInfo;
//출발지,도착지 마커
var marker_s, marker_e, marker_p;
//경로그림정보
var drawInfoArr = [];
var drawInfoArr2 = [];
var chktraffic = [];
var resultdrawArr = [];
var resultMarkerArr = [];
var lat_s =0;
var lon_s = 0;



function initTmap() {
    // 1. 지도 띄우기
    map = new Tmapv2.Map("map_div", {
        center : new Tmapv2.LatLng(37.49241689559544, 127.03171389453507),
        width : "100%",
        height : "400px",
        zoom : 11,
        zoomControl : true,
        scrollwheel : true
    });




    if (navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(
				function(position) {
					 lat_s = position.coords.latitude;
					 lon_s = position.coords.longitude;

					//팝업 생성
					var content = "<div style=' position: relative; border-bottom: 1px solid #dcdcdc; line-height: 18px; padding: 0 35px 2px 0;'>"
							+ "<div style='font-size: 12px; line-height: 15px;'>"
							+ "<span style='display: inline-block; width: 14px; height: 14px; background-image: url(/resources/images/common/icon_blet.png);"
							+ "vertical-align: middle; margin-right: 5px;'></span>현재위치!!"
							+ "</div>"
							+ "</div>";

					marker_s = new Tmapv2.Marker({
						position : new Tmapv2.LatLng(lat_s,lon_s),
						icon : "http://tmapapi.sktelecom.com/upload/tmap/marker/pin_r_m_s.png",
                        iconSize : new Tmapv2.Size(24, 38),
						map : map
					});

					InfoWindow = new Tmapv2.InfoWindow({
						position : new Tmapv2.LatLng(lat_s,lon_s),
						content : content,
						type : 2,
						map : map
					});
					map.setCenter(new Tmapv2.LatLng(lat_s,lon_s));
					map.setZoom(15);
				});


		}



    // 2. 시작, 도착 심볼찍기


        //  비동기로  x , y 넣기
        $.ajax({
            url :"/room/addressXY" ,
            data :{ "roomNo" : $("#road").val() } ,
            success:function(data){

                   var words = data.split(',');


                marker_e = new Tmapv2.Marker(
                    {
                        position : new Tmapv2.LatLng(words[1], words[2]),
                        icon : "http://tmapapi.sktelecom.com/upload/tmap/marker/pin_r_m_e.png",
                        iconSize : new Tmapv2.Size(24, 38),
                        map : map
                    });


                 // 3. 경로탐색 API 사용요청
                    $("#btn_select")
                        .click(
                            function() {

                                //기존 맵에 있던 정보들 초기화
                                resettingMap();

                                var searchOption = $("#selectLevel").val();

                                var trafficInfochk = $("#year").val();

                                //JSON TYPE EDIT [S]
                                $.ajax({
                                    type : "POST",
                                    url : "https://apis.openapi.sk.com/tmap/routes?version=1&format=json&callback=result",
                                    async : false,
                                    data : {
                                        "appKey" : "l7xx1a649f9e9e5642ed82f72ada2ff00e9d",
                                        "startX" : lon_s,
                                        "startY" : lat_s,
                                        "endX" : words[2],
                                        "endY" : words[1],
                                        "reqCoordType" : "WGS84GEO",
                                        "resCoordType" : "EPSG3857",
                                        "searchOption" : searchOption,
                                        "trafficInfo" : trafficInfochk
                                    },
                                    success : function(response) {

                                        var resultData = response.features;

                                        var tDistance = "총 거리 : "
                                                + (resultData[0].properties.totalDistance / 1000)
                                                        .toFixed(1) + "km,";
                                        var tTime = " 총 시간 : "
                                                + (resultData[0].properties.totalTime / 60)
                                                        .toFixed(0) + "분,";
                                        var taxiFare = " 예상 택시 요금 : "
                                                + resultData[0].properties.taxiFare
                                                + "원";

                                        $("#result").text(
                                                tDistance + tTime
                                                        + taxiFare);

                                        //교통정보 표출 옵션값을 체크
                                        if (trafficInfochk == "Y") {
                                            for ( var i in resultData) { //for문 [S]
                                                var geometry = resultData[i].geometry;
                                                var properties = resultData[i].properties;

                                                if (geometry.type == "LineString") {
                                                    //교통 정보도 담음
                                                    chktraffic
                                                            .push(geometry.traffic);
                                                    var sectionInfos = [];
                                                    var trafficArr = geometry.traffic;

                                                    for ( var j in geometry.coordinates) {
                                                        // 경로들의 결과값들을 포인트 객체로 변환
                                                        var latlng = new Tmapv2.Point(
                                                                geometry.coordinates[j][0],
                                                                geometry.coordinates[j][1]);
                                                        // 포인트 객체를 받아 좌표값으로 변환
                                                        var convertPoint = new Tmapv2.Projection.convertEPSG3857ToWGS84GEO(
                                                                latlng);

                                                        sectionInfos
                                                                .push(convertPoint);
                                                    }

                                                    drawLine(sectionInfos,
                                                            trafficArr);
                                                } else {

                                                    var markerImg = "";
                                                    var pType = "";

                                                    if (properties.pointType == "S") { //출발지 마커
                                                        markerImg = "http://tmapapi.sktelecom.com/upload/tmap/marker/pin_r_m_s.png";
                                                        pType = "S";
                                                    } else if (properties.pointType == "E") { //도착지 마커
                                                        markerImg = "http://tmapapi.sktelecom.com/upload/tmap/marker/pin_r_m_e.png";
                                                        pType = "E";
                                                    } else { //각 포인트 마커
                                                        markerImg = "http://topopen.tmap.co.kr/imgs/point.png";
                                                        pType = "P"
                                                    }

                                                    // 경로들의 결과값들을 포인트 객체로 변환
                                                    var latlon = new Tmapv2.Point(
                                                            geometry.coordinates[0],
                                                            geometry.coordinates[1]);
                                                    // 포인트 객체를 받아 좌표값으로 다시 변환
                                                    var convertPoint = new Tmapv2.Projection.convertEPSG3857ToWGS84GEO(
                                                            latlon);

                                                    var routeInfoObj = {
                                                        markerImage : markerImg,
                                                        lng : convertPoint._lng,
                                                        lat : convertPoint._lat,
                                                        pointType : pType
                                                    };
                                                    // 마커 추가
                                                    addMarkers(routeInfoObj);
                                                }
                                            }//for문 [E]

                                        } else {

                                            for ( var i in resultData) { //for문 [S]
                                                var geometry = resultData[i].geometry;
                                                var properties = resultData[i].properties;

                                                if (geometry.type == "LineString") {
                                                    for ( var j in geometry.coordinates) {
                                                        // 경로들의 결과값들을 포인트 객체로 변환
                                                        var latlng = new Tmapv2.Point(
                                                                geometry.coordinates[j][0],
                                                                geometry.coordinates[j][1]);
                                                        // 포인트 객체를 받아 좌표값으로 변환
                                                        var convertPoint = new Tmapv2.Projection.convertEPSG3857ToWGS84GEO(
                                                                latlng);
                                                        // 포인트객체의 정보로 좌표값 변환 객체로 저장
                                                        var convertChange = new Tmapv2.LatLng(
                                                                convertPoint._lat,
                                                                convertPoint._lng);
                                                        // 배열에 담기
                                                        drawInfoArr
                                                                .push(convertChange);
                                                    }
                                                    drawLine(drawInfoArr,
                                                            "0");
                                                } else {

                                                    var markerImg = "";
                                                    var pType = "";

                                                    if (properties.pointType == "S") { //출발지 마커
                                                        markerImg = "http://tmapapi.sktelecom.com/upload/tmap/marker/pin_r_m_s.png";
                                                        pType = "S";
                                                    } else if (properties.pointType == "E") { //도착지 마커
                                                        markerImg = "http://tmapapi.sktelecom.com/upload/tmap/marker/pin_r_m_e.png";
                                                        pType = "E";
                                                    } else { //각 포인트 마커
                                                        markerImg = "http://topopen.tmap.co.kr/imgs/point.png";
                                                        pType = "P"
                                                    }

                                                    // 경로들의 결과값들을 포인트 객체로 변환
                                                    var latlon = new Tmapv2.Point(
                                                            geometry.coordinates[0],
                                                            geometry.coordinates[1]);
                                                    // 포인트 객체를 받아 좌표값으로 다시 변환
                                                    var convertPoint = new Tmapv2.Projection.convertEPSG3857ToWGS84GEO(
                                                            latlon);

                                                    var routeInfoObj = {
                                                        markerImage : markerImg,
                                                        lng : convertPoint._lng,
                                                        lat : convertPoint._lat,
                                                        pointType : pType
                                                    };

                                                    // Marker 추가
                                                    addMarkers(routeInfoObj);
                                                }
                                            }//for문 [E]
                                        }
                                    },
                                    error : function(request, status, error) {
                                        console.log("code:"
                                                + request.status + "\n"
                                                + "message:"
                                                + request.responseText
                                                + "\n" + "error:" + error);
                                    }
                                });
                        //JSON TYPE EDIT [E]
                            });



            }
        });


}

function addComma(num) {
    var regexp = /\B(?=(\d{3})+(?!\d))/g;
    return num.toString().replace(regexp, ',');
}

//마커 생성하기
function addMarkers(infoObj) {
    var size = new Tmapv2.Size(24, 38);//아이콘 크기 설정합니다.

    if (infoObj.pointType == "P") { //포인트점일때는 아이콘 크기를 줄입니다.
        size = new Tmapv2.Size(8, 8);
    }

    marker_p = new Tmapv2.Marker({
        position : new Tmapv2.LatLng(infoObj.lat, infoObj.lng),
        icon : infoObj.markerImage,
        iconSize : size,
        map : map
    });

    resultMarkerArr.push(marker_p);
}

//라인그리기
function drawLine(arrPoint, traffic) {
    var polyline_;

    if (chktraffic.length != 0) {

        // 교통정보 혼잡도를 체크
        // strokeColor는 교통 정보상황에 다라서 변화
        // traffic :  0-정보없음, 1-원활, 2-서행, 3-지체, 4-정체  (black, green, yellow, orange, red)

        var lineColor = "";

        if (traffic != "0") {
            if (traffic.length == 0) { //length가 0인것은 교통정보가 없으므로 검은색으로 표시

                lineColor = "#06050D";
                //라인그리기[S]
                polyline_ = new Tmapv2.Polyline({
                    path : arrPoint,
                    strokeColor : lineColor,
                    strokeWeight : 6,
                    map : map
                });
                resultdrawArr.push(polyline_);
                //라인그리기[E]
            } else { //교통정보가 있음

                if (traffic[0][0] != 0) { //교통정보 시작인덱스가 0이 아닌경우
                    var trafficObject = "";
                    var tInfo = [];

                    for (var z = 0; z < traffic.length; z++) {
                        trafficObject = {
                            "startIndex" : traffic[z][0],
                            "endIndex" : traffic[z][1],
                            "trafficIndex" : traffic[z][2],
                        };
                        tInfo.push(trafficObject)
                    }

                    var noInfomationPoint = [];

                    for (var p = 0; p < tInfo[0].startIndex; p++) {
                        noInfomationPoint.push(arrPoint[p]);
                    }

                    //라인그리기[S]
                    polyline_ = new Tmapv2.Polyline({
                        path : noInfomationPoint,
                        strokeColor : "#06050D",
                        strokeWeight : 6,
                        map : map
                    });
                    //라인그리기[E]
                    resultdrawArr.push(polyline_);

                    for (var x = 0; x < tInfo.length; x++) {
                        var sectionPoint = []; //구간선언

                        for (var y = tInfo[x].startIndex; y <= tInfo[x].endIndex; y++) {
                            sectionPoint.push(arrPoint[y]);
                        }

                        if (tInfo[x].trafficIndex == 0) {
                            lineColor = "#06050D";
                        } else if (tInfo[x].trafficIndex == 1) {
                            lineColor = "#61AB25";
                        } else if (tInfo[x].trafficIndex == 2) {
                            lineColor = "#FFFF00";
                        } else if (tInfo[x].trafficIndex == 3) {
                            lineColor = "#E87506";
                        } else if (tInfo[x].trafficIndex == 4) {
                            lineColor = "#D61125";
                        }

                        //라인그리기[S]
                        polyline_ = new Tmapv2.Polyline({
                            path : sectionPoint,
                            strokeColor : lineColor,
                            strokeWeight : 6,
                            map : map
                        });
                        //라인그리기[E]
                        resultdrawArr.push(polyline_);
                    }
                } else { //0부터 시작하는 경우

                    var trafficObject = "";
                    var tInfo = [];

                    for (var z = 0; z < traffic.length; z++) {
                        trafficObject = {
                            "startIndex" : traffic[z][0],
                            "endIndex" : traffic[z][1],
                            "trafficIndex" : traffic[z][2],
                        };
                        tInfo.push(trafficObject)
                    }

                    for (var x = 0; x < tInfo.length; x++) {
                        var sectionPoint = []; //구간선언

                        for (var y = tInfo[x].startIndex; y <= tInfo[x].endIndex; y++) {
                            sectionPoint.push(arrPoint[y]);
                        }

                        if (tInfo[x].trafficIndex == 0) {
                            lineColor = "#06050D";
                        } else if (tInfo[x].trafficIndex == 1) {
                            lineColor = "#61AB25";
                        } else if (tInfo[x].trafficIndex == 2) {
                            lineColor = "#FFFF00";
                        } else if (tInfo[x].trafficIndex == 3) {
                            lineColor = "#E87506";
                        } else if (tInfo[x].trafficIndex == 4) {
                            lineColor = "#D61125";
                        }

                        //라인그리기[S]
                        polyline_ = new Tmapv2.Polyline({
                            path : sectionPoint,
                            strokeColor : lineColor,
                            strokeWeight : 6,
                            map : map
                        });
                        //라인그리기[E]
                        resultdrawArr.push(polyline_);
                    }
                }
            }
        } else {

        }
    } else {
        polyline_ = new Tmapv2.Polyline({
            path : arrPoint,
            strokeColor : "#DD0000",
            strokeWeight : 6,
            map : map
        });
        resultdrawArr.push(polyline_);
    }

}

//초기화 기능
function resettingMap() {
    //기존마커는 삭제
    marker_s.setMap(null);
    marker_e.setMap(null);

    if (resultMarkerArr.length > 0) {
        for (var i = 0; i < resultMarkerArr.length; i++) {
            resultMarkerArr[i].setMap(null);
        }
    }

    if (resultdrawArr.length > 0) {
        for (var i = 0; i < resultdrawArr.length; i++) {
            resultdrawArr[i].setMap(null);
        }
    }

    chktraffic = [];
    drawInfoArr = [];
    resultMarkerArr = [];
    resultdrawArr = [];
}