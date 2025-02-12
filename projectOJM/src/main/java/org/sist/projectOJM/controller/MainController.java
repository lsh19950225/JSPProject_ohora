package org.sist.projectOJM.controller;

import java.security.Principal;
import java.util.List;

import org.sist.projectOJM.restaurent.Restaurent;
import org.sist.projectOJM.restaurent.RestaurentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {
	
	
	private final RestaurentService restaurentService;
	
	
	@GetMapping( value = {"/", "/index"} )
	public String index( Model model, Principal principal) {
		System.out.println("index");
		boolean isLoggedIn = (principal != null);
		
	    
		List<Restaurent> t10 = this.restaurentService.getTop10();
		
		for (Restaurent r : t10) {
			System.out.println("@@@@@@@@@@@@@@@@탑텐:::"+r.getRname());
		}
		//탑텐 보내기
		model.addAttribute("t10",t10);
		
		//로그인 여부 보내기
		model.addAttribute("isLoggedIn", isLoggedIn);
		
		return "/OJM/test";
	} //

	
} // class
