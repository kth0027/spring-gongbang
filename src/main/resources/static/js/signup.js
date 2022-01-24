 $(function () {
            // 이메일 유효성검사
            $("#memail").keyup(function () {
                var emailj = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
                var memail = $("#memail").val();
                if (!emailj.test(memail)) {
                    $("#emailcheck").html("이메일 형식으로 입력해주세요");
                    $("#emailcheck").css('color', 'green');

                } else {
                    // 이메일 중복체크 비동기 통신
                    $.ajax({
                        url: "/member/emailcheck",
                        data: { "memail": memail },
                        success: function (result) {
                            if (result == 1) {
                                $("#emailcheck").html("현재 사용중인 이메일 입니다.");
                                $("#emailcheck").css('color', 'red');
                                $("#emailcheck").css('font-weight', 'bold');
                            } else {
                                $("#emailcheck").html("사용가능");
                                $("#emailcheck").css('color', 'blue');
                                $("#emailcheck").css('font-weight', 'bold');
                            }
                        } // success send
                    }); // ajax 함수  end
                }
            });


            // 패스워드 유효성검사
            $("#mpassword").keyup(function () {
                var pwj = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\d~!@#$%^&*()+|=]{8,15}$/;
                // 영대소문자+숫자+특수문자[ !@#$%^&*()+|= ] 8~15포함
                var mpassword = $("#mpassword").val();
                if (!pwj.test(mpassword)) {
                    $("#passwordcheck").html("영대소문자+숫자+특수문자[ !@#$%^&*()+|= ] 8~15포함");
                    $("#passwordcheck").css('color', 'green');

                } else {
                    $("#passwordcheck").html("사용가능");
                    $("#passwordcheck").css('color', 'blue');
                    $("#passwordcheck").css('font-weight', 'bold');
                }
            });

            // 패스워드 유효성검사
            $("#mpassword").keyup(function () {
                var pwj = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\d~!@#$%^&*()+|=]{8,15}$/;
                // 영대소문자+숫자+특수문자[ !@#$%^&*()+|= ] 8~15포함
                var mpassword = $("#mpassword").val();
                if (!pwj.test(mpassword)) {
                    $("#passwordcheck").html("영대소문자+숫자+특수문자[ !@#$%^&*()+|= ] 8~15포함");
                    $("#passwordcheck").css('color', 'green');

                } else {
                    $("#passwordcheck").html("사용가능");
                    $("#passwordcheck").css('color', 'blue');
                    $("#passwordcheck").css('font-weight', 'bold');
                }
            });

            // 패스워드 확인 유효성검사
            $("#mpasswordconfirm").keyup(function () {
                var pwj = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\d~!@#$%^&*()+|=]{8,16}$/;
                // 숫자', '문자', '특수문자' 무조건 1개 이상, 비밀번호 '최소 8자에서 최대 16자'까지 허용
                var mpassword = $("#mpassword").val();
                var mpasswordconfirm = $("#mpasswordconfirm").val();
                if (!pwj.test(mpasswordconfirm)) {
                    $("#passwordcheck").html("숫자', '문자', '특수문자' 포함 , '최소 8문자~16글자 허용.");
                    $("#passwordcheck").css('color', 'green');

                } else if (mpassword != mpasswordconfirm) {
                    $("#passwordcheck").html("서로 패스워드가 다릅니다.");
                    $("#passwordcheck").css('color', 'red');
                    $("#passwordcheck").css('font-weight', 'bold');

                } else {
                    $("#passwordcheck").html("사용가능");
                    $("#passwordcheck").css('color', 'blue');
                    $("#passwordcheck").css('font-weight', 'bold');
                }
            });

            // 이름 유효성검사
            $("#mname").keyup(function () {
                var namej = /^[A-Za-z가-힣]{1,15}$/;	// 이름 정규표현식
                var mname = $("#mname").val();
                if (!namej.test(mname)) {
                    $("#namecheck").html("영대문자/한글 1~15 허용");
                    $("#namecheck").css('color', 'green');
                    $("#namecheck").css('font-weight', 'bold');

                } else {
                    $("#namecheck").html("사용가능");
                    $("#namecheck").css('color', 'blue');
                    $("#namecheck").css('font-weight', 'bold');
                }
            });

            // 연락처 유효성검사
            $("#mphone").keyup(function () {
                var phonej = /^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$/; // 연락처
                var mphone = $("#mphone").val();
                if (!phonej.test(mphone)) {
                    $("#phonecheck").html("01X-XXXX-XXXX 형식으로 입력해주세요");
                    $("#phonecheck").css('color', 'red');
                    $("#phonecheck").css('font-weight', 'bold');

                } else {
                    $("#phonecheck").html("사용가능");
                    $("#phonecheck").css('color', 'blue');
                    $("#phonecheck").css('font-weight', 'bold');
                }
            });



           $("#formsubmit").click( function(){

                if( ! $('input[name=signupsign]').is(":checked") ) {
                     alert(" 회원가입 약관 동의시 회원가입이 가능합니다 . ");
                }
                else if( ! $('input[name=infosign]').is(":checked") ) {
                     alert(" 개인정보처리방침 동의시 회원가입이 가능합니다 . ");
                }
                else if(   $("#emailcheck").html() != "사용가능" ){
                     alert(" 이메일 불가능합니다 . ");
                }else if(  $("#passwordcheck").html() != "사용가능" ){
                     alert(" 패스워드 불가능합니다 . ");
                 }
                 else if(  $("#namecheck").html() != "사용가능" ){
                     alert(" 이름 불가능합니다 . ");
                  }
                  else if(  $("#phonecheck").html() != "사용가능" ){
                        alert(" 연락처가 불가능합니다 . ");


                 }else{
                        $("form").submit(); // 모든 유효성검사 통과시 폼 전송
                     }
           });







        }); // 함수 end