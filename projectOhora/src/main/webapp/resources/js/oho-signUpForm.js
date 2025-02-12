function pwdError() {
    let pwdAlert = $(".pwGuide");
    pwdAlert.text("");
    if (pwdAlert.hasClass("error")) {
        pwdAlert.text("(영문 대소문자/숫자/특수문자 중 2가지 이상 조합, 8자~16자)@")
    }
}

function isValidPasswd(passwd) {
    // 비밀번호 정규 표현식
    var passwdPattern = /^(?=.*[a-zA-Z])(?=.*\d|[!@#$%^&*()_+={}\[\]:;"'<>,.?/~`-])[A-Za-z\d!@#$%^&*()_+={}\[\]:;"'<>,.?/~`-]{8,16}$/;
    return passwdPattern.test(passwd);
}



function isValidId(id) {
	const idPattern = /^(?![0-9])(?=[a-z0-9]*$)[a-z][a-z0-9]{3,15}$/;
	return idPattern.test(id);
}

function idError() {
    let idAlert = $("#idMsg");
    idAlert.text("");
    if (idAlert.hasClass("error")) {
        idAlert.text("유효하지 않은 아이디 형식입니다")
    }
}

function phoneError() {
    let phoneAlert = $(".phoneMsg");
    phoneAlert.text("");
    if (phoneAlert.hasClass("error")) {
        phoneAlert.text("(영문 대소문자/숫자/특수문자 중 2가지 이상 조합, 8자~16자)@")
    }
}

function isValidPhone(phone){
	//이메일 정규 표현식
	var phonePattern = /^010-\d{4}-\d{4}/;
    return phonePattern.test(phone);
}


$(document).on('click', function(event) {
    //alert('페이지의 아무 곳이나 클릭했습니다!')
    let passwd = $("#passwd").val();
    let passwdAlert = $("#passwdMsg");
    if (!passwd) {
        passwdAlert.text("비밀");
        return;
    }


    if (!isValidEmail(email)) {
        $("#emailMsg").addClass("error")
    }else {
        $("#emailMsg").removeClass("error")
    }
    emailError();


});



function pwdcError() {
    let pwdAlert = $("#pwConfirmMsg");
    pwdAlert.text("");
    if (pwdAlert.hasClass("error")) {
        pwdAlert.text("비밀번호가 일치하지 않습니다")
    }
}


$(document).on('click', function(event) {
    //alert('페이지의 아무 곳이나 클릭했습니다!')
    let pwd = $("#passwd").val();
    let pwdc = $("#passwd-confirm").val();
    //alert(pwd)
    if (pwdc!=pwd) {
        $("#pwConfirmMsg").addClass("error")
    }else {
        $("#pwConfirmMsg").removeClass("error")
    }
    pwdcError();
});


function isValidEmail(email) {
    // 이메일 정규 표현식
    var emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailPattern.test(email);
}

function emailError() {
    let emailAlert = $("#emailMsg");
    emailAlert.text("");
    if (emailAlert.hasClass("error")) {
        emailAlert.text("유효하지 않은 이메일 형식입니다")
    }
}

$(document).on('click', function(event) {
    //alert('페이지의 아무 곳이나 클릭했습니다!')
    let email = $("#email").val();
    let emailAlert = $("#emailMsg");
    if (!email) {
        emailAlert.text("이메일을 입력하세요");
        return;
    }


    if (!isValidEmail(email)) {
        $("#emailMsg").addClass("error")
    }else {
        $("#emailMsg").removeClass("error")
    }
    emailError();
});

// 전체 동의


function allAgreeBtn() {

        // 전체 선택 체크박스 상태 확인
        const selectAllCheckbox = document.getElementById("AgreeAllCk");
        const checkboxes = document.querySelectorAll(".termCK");

        // 전체 선택 체크박스의 상태에 따라 개별 체크박스 선택/해제
        checkboxes.forEach((checkbox) => {
            checkbox.checked = selectAllCheckbox.checked;
        });

}

function updateSelectAll() {
    const selectAllCheckbox = document.getElementById("AgreeAllCk");
    const checkboxes = document.querySelectorAll(".termCK");

    // 개별 체크박스 중 하나라도 체크 해제된 것이 있는지 확인
    const allChecked = Array.from(checkboxes).every((checkbox) => checkbox.checked);

    // 모든 개별 체크박스가 선택되어 있으면 전체 선택 체크, 아니면 해제
    selectAllCheckbox.checked = allChecked;
}


//회원가입 서브밋

function copy_join_btn(event) {

	if( $("#email").attr('data-checked')=="false"  ) {
		alert("이메일 중복체크하세요")
		return;
	}
	if( $("#phone").attr('data-checked')=="false"  ) {
		alert("전화번호 중복체크하세요")
		return;
	}
	if( $("#user_login_id").attr('data-checked')=="false"  ) {
		alert("아이디 중복체크하세요")
		return;
	}
	let email = $("#email").val();
    let emailAlert = $("#emailMsg");
    if (!email) {
        emailAlert.text("이메일을 입력하세요");
        return;
    }

    if (!isValidEmail(email)) {
        $("#emailMsg").addClass("error")
    }else {
        $("#emailMsg").removeClass("error")
    }
    emailError();
	
	let pwd = $("#passwd").val();
    let pwdc = $("#passwd-confirm").val();
	let pwdcMsg = $("#pwConfirmMsg");
    //alert(pwd)
    if (pwdc!=pwd) {
        $("#pwConfirmMsg").addClass("error");
		
		
    }else {
        $("#pwConfirmMsg").removeClass("error");
    }
    pwdcError();
	
	
	let phone = $("#phone").val();
	let phoneMsg = $(".phoneMsg");
	
	
	 if (!isValidPhone(phone)) {
        $("#phoneMsg").addClass("error")
    }else {
        $("#phoneMsg").removeClass("error")
    }
	

	let termO = $("#termCk").is(':checked');
	let privacyO = $("#privacyCk").is(':checked');
	let consignO = $("#consignCk").is(':checked');
	let ageO = $("#ageCk").is(':checked');
		//회원가입 필수 입력창 체크
	let userIdO= $("#user_login_id").val();
	let userpwO= $("#passwd").val();
	let userpwcO= $("#passwd-confirm").val();
	let userNameO= $("#name").val();
	let userEmailO= $("#email").val();
	let userPhoneO= $("#phone").val();
	let userbirthyO= $("#birth-year").val();
	let userbirthmO= $("#birth-month").val();
	let userbirthdO= $("#birth-day").val();

	
/*	 if (!isValidId(userIdO)) {
        $("#phoneMsg").addClass("error")
    }else {
        $("#phoneMsg").removeClass("error")
    }*/



//alert(userNameO)
//alert(userbirthdO)
	
	if( termO && privacyO && consignO&&ageO ) {
		
		//alert("필수 체크 완료");
		} else {alert("필수 약관 동의가 필요합니다.") 
						return; }
		
	if(userIdO&&userpwO&&userpwcO&&userNameO&&userEmailO&&userPhoneO) {//필수항목 입력 체크
		//	alert("필수 입력 완료")	
		} else {alert("필수 항목을 입력하세요") 
					return;}
		
	if( userbirthyO >= 1900 && userbirthmO>=1 && userbirthmO<=12 &&  userbirthdO>=1 && userbirthdO<=31 ) {
		//alert("생일 입력 완료")	
	} else {alert("유효한 생일 형식이 아닙니다") 
				return;}
			
	if (emailAlert.hasClass("error")  || pwdcMsg.hasClass("error") || phoneMsg.hasClass("error")){
					alert("이메일 혹은 비밀번호 확인을 수정하세요")
					return;
				}else {
			
					alert("가입완료!")
					$("#joinForm").submit();
					}
}




//중복 메시지 띄우는 함수들..
function idMsg() {
	
	let idAlert = $("#idMsg");
    idAlert.removeClass("error")
	idAlert.addClass("error")
    if (idAlert.hasClass("error")) {
        idAlert.text("중복 아이디가 존재합니다")
		//alert("중복아디")
    }
}

function emailMsg() {
	
	let emAlert = $("#emailMsg");
    emAlert.text("");
	emAlert.removeClass("error")
	emAlert.addClass("error")
    if (emAlert.hasClass("error")) {
        emAlert.text("중복 이메일이 존재합니다")
    }
}

function phoneMsg() {
	
	let idAlert = $(".phoneMsg");
    idAlert.text("");
	idAlert.removeClass("error")
	idAlert.addClass("error")
    if (idAlert.hasClass("error")) {
        idAlert.text("중복 전화번호가 존재합니다")
    }
}






$('#passwd').on('focus', function() {
        $('.pwGuide2').text("");
    });


$('#phone').on('focus', function() {
        $('.phoneMsg').text("");
    });


// 중복 체크

function info_jungbok( id, msg ) {
		//$("#pwGuide").removeClass("error");


		let value = document.getElementById(id).value; 
		let phonevalue = $("#phone").val();
		let idvalue = $("#user_login_id").val();
		let passwd = $("#passwd").val();
			//폰도 있어야댐
		//alert(pwvalue)
		//alert(isValidPasswd(pwvalue))
if(isValidId(idvalue)) {
		
		if(isValidPasswd(passwd)){
				
	
			if(isValidPhone(phonevalue)  ) {
				$(".phoneMsg").removeClass("error")
	
				let value = document.getElementById(id).value;
				//alert( value )
				
				let msgTag = msg;
				//alert( msgTag )
			     
				 $.ajax({
					 url:"/projectOhora/board/idcheck.ajax" , 
					 dataType:"json",
					 type:"GET",
					 data: { msgTag : msgTag , val : value },
					 cache:false ,
					
				     // XMLHttpRequest == jqXHR
	                // textSatus
					 success: function ( data,  textStatus, jqXHR ){
						 // { "result":"true" }			
						if ( data.result == true && msgTag=="idMsg" ) {
							//alert( data.result );	
							//$("#idMsg").addClass("Dupl")
							idMsg();
						} else if( data.result == false && msgTag=="idMsg" ) {
							//중복 아닐떄
							$("#user_login_id").attr('data-checked', true)
							$("#idMsg").text("사용 가능한 아이디입니다.")
						}
						
						 else if( data.result == true && msgTag=="emailMsg" ){
							//$("#emailMsg").addClass("Dupl")
							emailMsg();
							//alert( data.result );	
						} 
						 else if( data.result == false && msgTag=="emailMsg" ){
							//중복 아닐떄
							$("#email").attr('data-checked', true)
							$("#emailMsg").text("사용 가능한 이메일입니다")
						} 
						
						
						else if( data.result == true && msgTag=="phoneMsg" ){
							//$(".phoneMsg").addClass("Dupl")
							phoneMsg();
							//alert( data.result );	
						}
						 else if( data.result == false && msgTag=="phoneMsg" ){
							$("#phone").attr("data-checked", true)
							$(".phoneMsg").text("사용 가능한 번호입니다.")
						} 
						 
					 },
					 error:function (){
						 alert("에러~~~ ");
					 }
					 
				 });
				
				}else {
				//alert("폰번호 형식 어긋남 ")
				$(".phoneMsg").text("전화번호 형식을 확인하세요. -로 구분해야합니다.").css("color", "red");
				}
				
			}else {
				$(".pwGuide2").text("비밀번호 형식을 확인하세요").css("color" ,"red");
			}
		
		} else {
			$("#idMsg").text("아이디는 영문소문자 또는 숫자 4~16자로 입력해주세요").css("color" ,"red");
		}
		
		
	}
		
		
//info_ jungbok


//확인받아놓고 장난질 하는거 막기
$('#user_login_id').on('input', function() {
        $('#user_login_id').attr('data-checked', 'false');
    });

$('#email').on('input', function() {
        $('#email').attr('data-checked', 'false');
    });


$('#phone').on('input', function() {
        $('#phone').attr('data-checked', 'false');
    });











