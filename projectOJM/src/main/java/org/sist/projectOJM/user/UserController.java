package org.sist.projectOJM.user;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	
	@GetMapping("/join")
	public String signup(UserCreateForm userCreateForm) {
		System.out.println("join");
		return "/user/join";
	}
	
	@PostMapping("/join")
	public String signup(
	        @Valid UserCreateForm userCreateForm,
	        BindingResult bindingResult) {

	    if (bindingResult.hasErrors()) {
	        System.out.println("join");
	        return "/user/join";
	    }

	    if (!userCreateForm.getUserPassword1().equals(userCreateForm.getUserPassword2())) {
	        bindingResult.rejectValue("userPassword2", "passwordInCorrect", 
	                "2개의 패스워드가 일치하지 않습니다.");
	        System.out.println("join");
	        return "/user/join";
	    }

	    try {
	        this.userService.create(
	                userCreateForm.getUserLoginId(), 
	                userCreateForm.getUserName(),
	                userCreateForm.getUserEmail(), 
	                userCreateForm.getUserPassword1()
	        );

	    } catch (DataIntegrityViolationException e) {
	        // 예외 메시지나 코드 확인
	        e.printStackTrace();

	        // 예외 메시지에서 "ID" 또는 "Email"을 포함하는지 확인하고 적절한 메시지 처리
	        if (e.getMessage().contains("USER_LOGIN_ID")) {
	            bindingResult.rejectValue("userLoginId", "idExists", "이미 존재하는 아이디입니다.");
	        } else if (e.getMessage().contains("USER_EMAIL")) {
	            bindingResult.rejectValue("userEmail", "emailExists", "이미 존재하는 이메일입니다.");
	        } else {
	            bindingResult.reject("joinFailed", "이미 등록된 사용자입니다.");
	        }

	        System.out.println("join");
	        return "/user/join";
	    } catch (Exception e) {
	        e.printStackTrace();
	        bindingResult.reject("joinFailed", e.getMessage());
	        System.out.println("join");
	        return "/user/join";
	    }

	    return "redirect:/user/login"; // 메인페이지로 간다...
	}

	
	@GetMapping("/login")
	public String login() {
		System.out.println("login");
		return "/user/login";
		
	}
	
	
	
} // class



