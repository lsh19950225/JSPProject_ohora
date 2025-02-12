package org.sist.projectOJM.restaurent;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RestaurentAjaxController {
	
	private final RestaurentService restaurentService;
	
	@GetMapping( value = {"/main/top10"} )
	public List<Restaurent> getTop10JSON() {
		
		List<Restaurent> t10 = this.restaurentService.getTop10();
		
		
		return t10;
	}
	
	
	
} // class
