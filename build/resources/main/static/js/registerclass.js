// tab 이벤트 + summernote + checkbox 클릭 이벤트 핸들러
$(function(){

    // 클래스 등록 시, 입력한 값에 대한 유효성 검사 시작
    // @Author : 김정진
    // @Date : 2022-02-16

    // 1. 클래스 제목 입력에 대한 유효성 검사
    $("#roomTitle").keyup(function(){
        var title = $("#roomTitle").val();

        var regTitle =/^[a-zA-Z가-힣]{1,50}$/;
        // 1. 글자수를 카운트해서 view 에 보여줍니다.
        var titleLength = getTextLength(title);
        $("#title-key-count").html(titleLength);
        // 2. 글자수는 50개로 제한을 둡니다.
        if(titleLength > 50){
            alert("제목은 50자 이하로 입력가능합니다.");
            // 3. 글자수를 초과했다면 맨 마지막 글자를 지웁니다.
            $("#roomTitle").val("");
            var trimmedTitle = title.substring(0, 40);
            $("#roomTitle").val(trimmedTitle);
        }
        if(!regTitle.test(title)){
            $("#title-result").html("유효성 검사 실패");
        }
    });

    // 2. 클래스 내용에 대한 유효성 검사
    $("#addClassInfo").keyup(function(){
        var content = $("#addClassInfo").val();
        var regContent = /^[a-zA-Z가-힣]{1,100}$/;
        // 1. 입력받은 글자수를 view 에 보여줍니다.
        var contentLength = getTextLength(content);
        $("#content-key-count").html(contentLength);
        if(contentLength > 100){
            alert("클래스 간단 소개는 100자 이하로 입력가능합니다.");
            $("#roomContent").val("");
            var trimmedContent = content.substring(0, 90);
            $("#roomContent").val(trimmedContent);
        }
        if(!regContent.test(content)){
            $("#content-result").html("유효성 검사 실패");
        }
    });

    // 3. 사진 입력에 대한 유효성 검사
        // 1. 무조건 사진을 1장이상 등록해야합니다.


    // 4. 클래스 상세 소개에 대한 유효성 검사
        // 1. summernote 에 내용을 최소 50글자 이상 입력해야합니다.

    // 5. 주소 입력에 대한 유효성 검사
        // 1. 무조건 주소를 선택해야합니다.


    $("ul.tabs li").click(function(){
        var tabId = $(this).attr("data-tab");

        $("ul.tabs li").removeClass("current");
        $(".tab-content").removeClass("current");

        $(this).addClass("current");
        $("#"+ tabId).addClass("current");
    });

    $('#summernote').summernote({
        lang: 'ko-KR',  // 메뉴 한글 버전 ,
        minHeight: 400, // 최소 높이
        maxHeight: null,
        placeholder: "내용 입력"

    });

    // 02-17 조지훈
    $('#summernoteupdate').summernote({
            lang: 'ko-KR',  // 메뉴 한글 버전 ,
            minHeight: 400, // 최소 높이
            maxHeight: null,
            placeholder: "내용 입력"

    });

    $("#formCheck1").on("change", function(e) {
        if( $("#formCheck1").is(":checked")){
            $("#checkBox1").val("반려동물 가능합니다");
        }
    });
    $("#formCheck2").on("change", function(e) {
        if( $("#formCheck2").is(":checked")){
            $("#checkBox2").val("와이파이를 제공합니다");
        }
    });

    $("#formCheck3").on("change", function(e) {
        if( $("#formCheck3").is(":checked")){
            $("#checkBox3").val("주차공간을 제공합니다");
        }
    });
});

function registerCheck(){


}

// 문자열 길이 구하기
function getTextLength(str){
    var len = 0;
    for(var i=0; i<str.length; i++){
        if(escape(str.charAt(i)).length == 6){
            len++;
        }
        len++;
    }
    return len;
}
// 사진 미리보기 및 업로드 js 시작
var input = document.getElementById("image-input");
var initLabel = document.getElementById("image-label");

$("#image-input")[0].addEventListener("change", (event) => {
    const files = changeEvent(event);
    handleUpdate(files);
});

initLabel.addEventListener("mouseover", (event) => {
    event.preventDefault();
    const label = document.getElementById("image-label");
    label?.classList.add("image-label--hover");
});

initLabel.addEventListener("mouseout", (event) => {
    event.preventDefault();
    const label = document.getElementById("image-label");
    label?.classList.remove("image-label--hover");
});

document.addEventListener("dragenter", (event) => {
    event.preventDefault();
        console.log("dragenter");
        if (event.target.className === "inner") {
        event.target.style.background = "#fff1ed";
    }
});

document.addEventListener("dragover", (event) => {
    console.log("dragover");
    event.preventDefault();
});

document.addEventListener("dragleave", (event) => {
    event.preventDefault();
    console.log("dragleave");
    if (event.target.className === "inner") {
        event.target.style.background = "#fff1ed";
    }
});

document.addEventListener("drop", (event) => {
    event.preventDefault();
    console.log("drop");
    if (event.target.className === "inner") {
        const files = event.dataTransfer?.files;
        event.target.style.background = "#fff1ed";
        handleUpdate([...files]);
    }
});

function changeEvent(event){
    const { target } = event;
    return [...target.files];
};

function handleUpdate(fileList){
    const preview = document.getElementById("preview");
    fileList.forEach((file) => {
        const reader = new FileReader();
        reader.addEventListener("load", (event) => {
            const img = el("img", {
                className: "embed-img img-fluid",
                src: event.target?.result,
            });
            const imgContainer = el("div", { className: "container-img" }, img);
                preview.append(imgContainer);
            });
        reader.readAsDataURL(file);
    });
};

function el(nodeName, attributes, ...children) {
    const node =
    nodeName === "fragment"
    ? document.createDocumentFragment()
    : document.createElement(nodeName);

    Object.entries(attributes).forEach(([key, value]) => {
        if (key === "events") {
            Object.entries(value).forEach(([type, listener]) => {
            node.addEventListener(type, listener);
            });
        } else if (key in node) {
            try {
            node[key] = value;
            } catch (err) {
            node.setAttribute(key, value);
            }
        } else {
        node.setAttribute(key, value);
        }
    });

    children.forEach((childNode) => {
        if (typeof childNode === "string") {
            node.appendChild(document.createTextNode(childNode));
        } else {
            node.appendChild(childNode);
        }
    });

  return node;
}
// 사진 미리보기 및 업로드 js 종료

// 다음 주소 등록 api 시작
var mapContainer = document.getElementById('map'),
    mapOption = {
        center: new daum.maps.LatLng(37.537187, 127.005476),
        level: 5
    };
var map = new daum.maps.Map(mapContainer, mapOption);
var geocoder = new daum.maps.services.Geocoder();
var marker = new daum.maps.Marker({
    position: new daum.maps.LatLng(37.537187, 127.005476),
    map: map
});

function sample5_execDaumPostcode() {
    new daum.Postcode({
        oncomplete: function(data) {
            var addr = data.address; // 최종 주소 변수

            // 주소 정보를 해당 필드에 넣는다.
            document.getElementById("sample5_address").value = addr;
            // 주소로 상세 정보를 검색
            geocoder.addressSearch(data.address, function(results, status) {
                // 정상적으로 검색이 완료됐으면
                if (status === daum.maps.services.Status.OK) {

                    var result = results[0]; //첫번째 결과의 값을 활용

                    // 해당 주소에 대한 좌표를 받아서
                    var coords = new daum.maps.LatLng(result.y, result.x);

                    // 해당 주소에 대한 위도, 경도 값을 addressx, addressy 에 각각 저장합니다.
                    $("#addressY").val(result.y);
                    $("#addressX").val(result.x);

                    // 지도를 보여준다.
                    mapContainer.style.display = "block";
                    map.relayout();
                    // 지도 중심을 변경한다.
                    map.setCenter(coords);
                    // 마커를 결과값으로 받은 위치로 옮긴다.
                    marker.setPosition(coords)
                }
            });
        }
    }).open();
}

// 다음 주소 등록 api 종료

// 클래스 등록 유효성 검사 시작
function classRegisterCheck(){

    // 업로드한 이미지에 대한 유효성 검사는 Controller 에서 수행합니다.



}

// 클래스 등록 유효성 검사 종료

