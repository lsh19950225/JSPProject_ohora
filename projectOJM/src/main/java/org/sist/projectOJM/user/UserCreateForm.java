package org.sist.projectOJM.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateForm {

	@NotEmpty(message = "이름은 필수항목입니다.")
	private String userName;
	
	@Size(max = 25, min = 3)
	@NotEmpty(message = "사용자ID는 필수항목입니다.")
	private String userLoginId;
	
	@Size(min = 8, max = 16, message = "비밀번호는 8자 이상 16자 이하이어야 합니다.")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,16}$", message = "비밀번호는 영문 대소문자, 숫자 또는 특수문자 중 2가지 이상 조합, 8자 이상 16자 이하로 설정해야 합니다.")
	@NotEmpty(message = "비밀번호는 필수항목입니다.")
	private String userPassword1;
	
	@NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
	private String userPassword2;
	
	@NotEmpty(message = "이메일은 필수항목입니다.")
	@Email
	private String userEmail;
	
}

