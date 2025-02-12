 /* $("#btn_payment").on("click", function(event) {
    if ($("#rzipcode1").val() && $("#raddr1").val() && $("#rphone2_2").val() && $("#rphone2_3").val()
        && $("#rname").val() && $("#email1").val()) {

        // 이메일 형식 검사
        const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if (!emailPattern.test($("#email1").val())) {
            alert("올바른 이메일 형식을 입력하세요");
            event.preventDefault();
			return false;
        }

        $("#order_form").submit();
    } else {
        alert("배송지를 입력하세요");
        event.preventDefault();
    }
});*/
$(document).ready(function() {
    // 최근 배송지 탭 클릭시
    $("a#preaddr").on("click", function(){
        $("#recent-addr-info").show();
        $("#selfInputAddr-form").hide();
        $("#addrinput2").removeClass("selected");
        $("#addrinput1").addClass("selected");
    });

    // 직접입력 탭 클릭시
    $("a#selfaddr").on("click", function(){
        $("#recent-addr-info").hide();
        $("#selfInputAddr-form").show();
        $("#addrinput1").removeClass("selected");
        $("#addrinput2").addClass("selected");
    });

    // 적립금 전액사용 버튼 클릭시
    $("#btn_point").on("click", function(){
        $("#input_point").val($(".UseablePoint").data('point'));
    });

    // 적립금 입력값 변경시
    document.querySelector('#input_point').addEventListener('change', function() {
        validateAndReplace(this);
    });

    // 적립금 유효성 검사
    function validateAndReplace(input) {
        let userPoint = $(".UseablePoint").data('point');
        if (isNaN(input.value) || input.value.trim() === "" || Number(input.value) < 3000 || Number(input.value) > userPoint) {
            input.value = 0;
        }
    }

    // 카카오 우편번호 검색 API
    function postCode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분
                var addr = ''; // 주소 변수
                var extraAddr = ''; // 참고항목 변수

                //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
                }

                // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다
                if(data.userSelectedType === 'R'){
                    // 법정동명이 있을 경우 추가한다
                    if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                        extraAddr += data.bname;
                    }
                    // 건물명이 있고, 공동주택일 경우 추가한다
                    if(data.buildingName !== '' && data.apartment === 'Y'){
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다
                    if(extraAddr !== ''){
                        extraAddr = ' (' + extraAddr + ')';
                    }
                    document.getElementById("raddr2").value = extraAddr;
                } else {
                    document.getElementById("raddr2").value = '';
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다
                document.getElementById('rzipcode1').value = data.zonecode;
                document.getElementById("raddr1").value = addr;
                // 커서를 상세주소 필드로 이동한다
                document.getElementById("raddr2").focus();
            }
        }).open();
    }
    
    // postCode 함수를 전역으로 노출
    window.postCode = postCode;
});