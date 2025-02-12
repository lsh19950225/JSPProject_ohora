<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> <%@
taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> <% String
contextPath = request.getContextPath(); String emailOrPhoneCheck = (String)
request.getAttribute("contactMethod"); System.out.println("emailOrPhoneCheck : "
+ emailOrPhoneCheck); String email = (String) request.getAttribute("email");
System.out.println("@@@@@@@@@@@@@@ : " + email); String phone =
(String)request.getAttribute("phone"); System.out.println("@@@@@@@@@@@@@@ : " +
phone); String userID = (String)request.getAttribute("userID");
System.out.println("@@@@@@@@@@@@@@ : " + userID); %>

<!DOCTYPE html>
<html lang="ko">
  <head>
    <meta charset="UTF-8" />
    <title>오호라 팀 프로젝트</title>
    <link
      rel="shortcut icon"
      type="image/x-icon"
      href="http://localhost/jspPro/images/SiSt.ico"
    />
    <link
      href="https://fonts.googleapis.com/css?family=Libre+Baskerville|Noto+Sans+KR&display=swap"
      rel="stylesheet"
    />
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="google" content="notranslate" />

    <style>
      .container {
        overflow: hidden;
        width: 1920px;
        height: 670px;
        position: absolute;
      }

      .title {
        color: rgba(0, 0, 0, 1);
        width: 138.04px;
        height: 37px;
        position: absolute;
        left: 811.84px;
        top: 130px;
        font-family: "Libre Baskerville", serif;
        text-align: center;
        font-size: 30px;
        white-space: nowrap;
        font-weight: bold;
      }

      .description {
        color: rgba(119, 119, 119, 1);
        width: 496.52px;
        height: 35px;
        position: absolute;
        left: 711.84px;
        top: 205px;
        font-family: "Noto Sans KR", sans-serif;
        text-align: center;
        font-size: 18px;
        line-height: 1.5;
        font-weight: bold;
      }

      .form-container {
        width: 500px;
        height: auto;
        position: absolute;
        left: 710px;
        top: 261px;
      }

      .label-container {
        background-color: rgba(241, 241, 241, 1);
        width: 160px;
        height: 34px;
        position: absolute;
        display: flex;
        align-items: center;
        padding-left: 10px;
      }

      .label-text {
        color: rgba(0, 0, 0, 1);
        font-family: "Noto Sans KR", sans-serif;
        text-align: left;
        font-size: 12px;
        line-height: 1.5;
      }

      .radio-group {
        display: flex;
        align-items: center;
        gap: 5px;
        position: absolute;
        left: 170px;
        top: 0;
      }

      .radio-label {
        color: rgba(0, 0, 0, 1);
        font-family: "Noto Sans KR", sans-serif;
        font-size: 12px;
        line-height: 1.5;
      }

      .input-field {
        width: 330px;
        height: 34px;
        position: absolute;
        left: 170px;
        border: 1px solid rgba(221, 221, 221, 1);
        padding: 0 10px;
        font-size: 12px;
        font-family: "Noto Sans KR", sans-serif;
        box-sizing: border-box;
      }

      .button-container {
        width: 500px;
        height: 50px;
        position: absolute;
        top: 250px;
        background-color: rgba(0, 0, 0, 1);
        border: 1px solid rgba(0, 0, 0, 1);
        display: flex;
        align-items: center;
        justify-content: center;
      }

      .button-container button {
        background-color: #000000;
        border: none;
        color: rgba(255, 255, 255, 1);
        font-family: "Noto Sans KR", sans-serif;
        font-size: 18px;
        cursor: pointer;
      }

      .cancel-button-container {
        width: 500px;
        height: 50px;
        position: absolute;
        top: 302px;
        background-color: #fff;
        border: 1px solid #ccc;
        display: flex;
        align-items: center;
        justify-content: center;
      }

      .cancel-button {
        background-color: transparent;
        border: none;
        color: rgba(0, 0, 0, 1);
        font-family: "Noto Sans KR", sans-serif;
        font-size: 18px;
        cursor: pointer;
      }

      #abcde {
        cursor: pointer;
      }

      #trueBtn {
        cursor: pointer;
      }

      #failBtn {
        cursor: pointer;
      }
    </style>
  </head>
  <body>
    <div class="container"></div>
    <span class="title">PASSWORD FIND</span>
    <span class="description">본인확인 인증</span>

    <form
      id="formUserID"
      class="form-container"
      method="post"
      action="/findPwd3.htm"
    >
      <!-- 본인확인 인증 라디오 버튼 -->
      <div class="label-container" style="top: 10px">
        <span class="label-text">본인확인 인증</span>
      </div>

      <div class="radio-group" style="top: 10px">
        <input
          type="radio"
          id="phone"
          name="verification_method"
          value="phone"
        />
        <label for="phone" class="radio-label">휴대폰</label>

        <input
          type="radio"
          id="email"
          name="verification_method"
          value="email"
        />
        <label for="email" class="radio-label">이메일</label>
      </div>

      <!-- contact 입력란 -->
      <div class="label-container" style="top: 55px">
        <% if ( emailOrPhoneCheck.equals("phone") ) { %>
        <span class="label-text">휴대폰 번호</span>
        <% } else { %>
        <span class="label-text">이메일</span>
        <% } // if else %>
      </div>

      <input
        type="text"
        class="input-field"
        id="value"
        style="top: 55px"
        name="number"
        value=""
        readonly
      />
      
      <button
        type="button"
        id="getCode"
        class="input-field"
        style="top: 55px; left: 510px; width: 100px; cursor: pointer"
        onclick="getVerifyCode()"
      >
        인증번호 받기
      </button>
      
      <!-- 인증번호 받기 -->
    <script>
	  function getVerifyCode() {
	    // AJAX 요청
	    $.ajax({
	      url: "/ajax/getCode.ajax", 
	      type: "GET", 
	      dataType: "json",
	      success: function (response) {
	        // 서버에서 받은 응답을 분기하여 처리
	        if (response == 1) {
	          // 인증번호 발급 성공
	          alert("인증번호가 발송되었습니다.");
	        } else if (response == 0) {
	          // 인증번호 발급 실패
	          alert("인증번호 발송에 실패했습니다.");
	        } else {
	          // 예기치 않은 응답 처리
	          alert("알 수 없는 오류가 발생했습니다.");
	        }
	      },
	      error: function (xhr, status, error) {
	        console.error("에러 발생: ", error);
	        alert("인증번호를 받는 데 문제가 발생했습니다.");
	      },
	    });
	  }
	</script>

      <!-- 인증번호 입력란 -->
      <div class="label-container" style="top: 115px">
        <span class="label-text">인증번호</span>
      </div>

      <!-- 내가 입력한 값 -->
      <input
        type="text"
        id="myCode"
        class="input-field"
        style="top: 115px"
        name="myCode"
        placeholder="인증번호를 입력하세요"
      />

      <button
        type="button"
        id="checkCode"
        class="input-field"
        style="top: 115px; left: 510px; width: 100px; cursor: pointer"
        onclick="checkVerifyCode()"
      >
        인증번호 확인
      </button>

      <!-- 안내 문구 -->
      <div
        class="label-text"
        style="position: absolute; top: 165px; left: 170px; font-size: 10px"
      >
        1회 발송된 인증번호의 유효 시간은 3분이며,<br />
        1회 인증번호 발송 후 30초 이후에 재전송이 가능합니다.
      </div>

      <!-- 확인 버튼 -->
      <div class="button-container" id="trueBtn">
        <button type="button" class="button-text" disabled>확인</button>
      </div>

      <!-- 취소 버튼 -->
      <div class="cancel-button-container" id="failBtn">
        <button type="button" class="cancel-button">취소</button>
      </div>
      <input type="hidden" name="userID" value="<%= userID %>" />
      <input
        type="hidden"
        name="${_csrf.parameterName}"
        value="${_csrf.token}"
      />
    </form>

    <script>
      // 인증번호 확인 함수
      function checkVerifyCode() {
        let myCode = $("#myCode").val(); // 입력된 인증 코드 가져오기

        // 입력된 인증 코드가 비어있지 않으면 서버로 요청 보내기
        if (myCode == "") {
          alert("인증번호가 비어있습니다."); // 비어있는 경우 처리
          return;
        }

        // 인증번호 확인을 위한 AJAX 요청
        $.ajax({
          url: "/ajax/checkCode.ajax",
          type: "GET",
          data: {
            myCode: myCode,
          },
          dataType: "json",
          success: function (response) {
            if (response == 1) {
              alert("인증번호 확인이 완료되었습니다.");
              // 인증번호가 맞으면 확인 버튼을 활성화
              $("#trueBtn .button-text").prop("disabled", false);
            } else if (response == 0) {
              alert("유효하지 않은 인증번호입니다.");
              // 인증번호가 틀리면 확인 버튼을 비활성화
              $("#trueBtn .button-text").prop("disabled", true);
            }
          },
          error: function (xhr, status, error) {
            console.error("에러 발생: ", error);
            alert("인증번호 확인에 문제가 발생했습니다.");
          },
        });
      }

      // 확인 버튼 클릭 시 폼 제출
      $("#trueBtn").on("click", function () {
        // 인증번호가 입력되었고 확인 버튼이 활성화된 경우에만 폼 제출
        if (
          $("#texttext").val() != "" &&
          !$("#trueBtn .button-text").prop("disabled")
        ) {
          alert("본인인증이 완료되었습니다."); // 성공 메시지 띄우기
          $("#formUserID").submit(); // 인증이 완료된 후 폼 제출
        } else {
          alert("인증번호를 확인해주세요."); // 인증번호가 입력되지 않았거나 실패한 경우
        }
      });
    </script>

    <!-- 이메일 또는 폰에 맞게 라디오 버튼 활성화 및 비활성화 / 값 넣기 -->
    <script>
      var emailOrPhoneCheck = "<%= emailOrPhoneCheck != null ? emailOrPhoneCheck : "" %>";
      if (emailOrPhoneCheck === "email") {
          $("#email").prop("checked", true);
          $("#phone").prop("disabled", true);
          $("#value").val("<%= email %>");
      } else {
          $("#phone").prop("checked", true);
          $("#email").prop("disabled", true);
          $("#value").val("<%= phone %>");
      }
    </script>
  </body>
</html>
