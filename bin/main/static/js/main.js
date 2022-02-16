// 지역 선택, 카테고리 선택 버튼 토글

$(function () {

	$('#slider-div1').slick({
		slide: 'div',		//슬라이드 되어야 할 태그 ex) div, li
		infinite: true, 	//무한 반복 옵션
		slidesToShow: 4,		// 한 화면에 보여질 컨텐츠 개수
		slidesToScroll: 2,		//스크롤 한번에 움직일 컨텐츠 개수
		speed: 100,	 // 다음 버튼 누르고 다음 화면 뜨는데까지 걸리는 시간(ms)
		arrows: true, 		// 옆으로 이동하는 화살표 표시 여부
		dots: true, 		// 스크롤바 아래 점으로 페이지네이션 여부
		autoplay: true,			// 자동 스크롤 사용 여부
		autoplaySpeed: 10000, 		// 자동 스크롤 시 다음으로 넘어가는데 걸리는 시간 (ms)
		pauseOnHover: true,		// 슬라이드 이동	시 마우스 호버하면 슬라이더 멈추게 설정
		vertical: false,		// 세로 방향 슬라이드 옵션
		prevArrow: "<button type='button' class='slick-prev'>Previous</button>",		// 이전 화살표 모양 설정
		nextArrow: "<button type='button' class='slick-next'>Next</button>",		// 다음 화살표 모양 설정
		dotsClass: "slick-dots", 	//아래 나오는 페이지네이션(점) css class 지정
		draggable: true, 	//드래그 가능 여부

		responsive: [ // 반응형 웹 구현 옵션
			{
				breakpoint: 960, //화면 사이즈 960px
				settings: {
					//위에 옵션이 디폴트 , 여기에 추가하면 그걸로 변경
					slidesToShow: 3
				}
			},
			{
				breakpoint: 768, //화면 사이즈 768px
				settings: {
					//위에 옵션이 디폴트 , 여기에 추가하면 그걸로 변경
					slidesToShow: 2
				}
			}
		]
	});


	$("#localBtn").on("click", function () {
		$("#slider-div1").show();
		$("#slider-div2").hide();
		// $('#slider-div1').slick({
		// 	slide: 'div',		//슬라이드 되어야 할 태그 ex) div, li
		// 	infinite: true, 	//무한 반복 옵션
		// 	slidesToShow: 4,		// 한 화면에 보여질 컨텐츠 개수
		// 	slidesToScroll: 2,		//스크롤 한번에 움직일 컨텐츠 개수
		// 	speed: 100,	 // 다음 버튼 누르고 다음 화면 뜨는데까지 걸리는 시간(ms)
		// 	arrows: true, 		// 옆으로 이동하는 화살표 표시 여부
		// 	dots: true, 		// 스크롤바 아래 점으로 페이지네이션 여부
		// 	autoplay: true,			// 자동 스크롤 사용 여부
		// 	autoplaySpeed: 10000, 		// 자동 스크롤 시 다음으로 넘어가는데 걸리는 시간 (ms)
		// 	pauseOnHover: true,		// 슬라이드 이동	시 마우스 호버하면 슬라이더 멈추게 설정
		// 	vertical: false,		// 세로 방향 슬라이드 옵션
		// 	prevArrow: "<button type='button' class='slick-prev'>Previous</button>",		// 이전 화살표 모양 설정
		// 	nextArrow: "<button type='button' class='slick-next'>Next</button>",		// 다음 화살표 모양 설정
		// 	dotsClass: "slick-dots", 	//아래 나오는 페이지네이션(점) css class 지정
		// 	draggable: true, 	//드래그 가능 여부

		// 	responsive: [ // 반응형 웹 구현 옵션
		// 		{
		// 			breakpoint: 960, //화면 사이즈 960px
		// 			settings: {
		// 				//위에 옵션이 디폴트 , 여기에 추가하면 그걸로 변경
		// 				slidesToShow: 3
		// 			}
		// 		},
		// 		{
		// 			breakpoint: 768, //화면 사이즈 768px
		// 			settings: {
		// 				//위에 옵션이 디폴트 , 여기에 추가하면 그걸로 변경
		// 				slidesToShow: 2
		// 			}
		// 		}
		// 	]
		// });

	});

	$("#categoryBtn").on("click", function () {
		$("#slider-div1").hide();
		$("#slider-div2").show();
		$('#slider-div2').slick({
			slide: 'div',		//슬라이드 되어야 할 태그 ex) div, li
			infinite: true, 	//무한 반복 옵션
			slidesToShow: 4,		// 한 화면에 보여질 컨텐츠 개수
			slidesToScroll: 2,		//스크롤 한번에 움직일 컨텐츠 개수
			speed: 100,	 // 다음 버튼 누르고 다음 화면 뜨는데까지 걸리는 시간(ms)
			arrows: true, 		// 옆으로 이동하는 화살표 표시 여부
			dots: true, 		// 스크롤바 아래 점으로 페이지네이션 여부
			autoplay: true,			// 자동 스크롤 사용 여부
			autoplaySpeed: 10000, 		// 자동 스크롤 시 다음으로 넘어가는데 걸리는 시간 (ms)
			pauseOnHover: true,		// 슬라이드 이동	시 마우스 호버하면 슬라이더 멈추게 설정
			vertical: false,		// 세로 방향 슬라이드 옵션
			prevArrow: "<button type='button' class='slick-prev'>Previous</button>",		// 이전 화살표 모양 설정
			nextArrow: "<button type='button' class='slick-next'>Next</button>",		// 다음 화살표 모양 설정
			dotsClass: "slick-dots", 	//아래 나오는 페이지네이션(점) css class 지정
			draggable: true, 	//드래그 가능 여부

			responsive: [ // 반응형 웹 구현 옵션
				{
					breakpoint: 960, //화면 사이즈 960px
					settings: {
						//위에 옵션이 디폴트 , 여기에 추가하면 그걸로 변경
						slidesToShow: 3
					}
				},
				{
					breakpoint: 768, //화면 사이즈 768px
					settings: {
						//위에 옵션이 디폴트 , 여기에 추가하면 그걸로 변경
						slidesToShow: 2
					}
				}
			]

		});
	})

});



// 쪽지 카운트 세기
$(function () {
	$.ajax({
		url: "/nreadcount",
		success: function (data) {

		}
	});
});

function roomLike(roomNo,memberNo) { // 답변하기 클릭 이벤트
    $.ajax({
        url:"/roomLike/roomLike",
        data: {"roomNo": roomNo,"memberNo":memberNo},
        success: function (data) {
            if(data==1){
                alert("좋아요");
                $("#like").html("♥");

            } else if(data==2){
                 alert("싫어요");
                  $("#like").html("♡");
            }

        }


    });

}

/*
    // 리뷰 버튼 클릭 이벤트
function reviewwrite(roomNo){
   var reviewContent =$("#reviewContent").val();

   $.ajax({
    url: '/room/reviewwrite',
    data:{"roomNo":roomNo , "reviewContent":reviewContent},
    success: function(data){
        if(data==1){
            alert("정상적으로 등록하셧습니다.");
             $("#reviewContent").val(""); // 내용물 초기화
             $("#reviewmodal").modal("hide"); // 모달 종료
        } else if(data==2){
            alert("로그인 후 등록이 가능합니다.");
            // 로그인창 모달띄우기
             $("#reviewContent").val(""); // 내용물 초기화
             $("#reviewmodal").modal("hide"); // 모달 종료

        }
    }

   });
}*/
