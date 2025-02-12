package org.sist.projectOJM.rank;

import java.util.List;

import org.sist.projectOJM.restaurent.Restaurent;
import org.sist.projectOJM.restaurent.RestaurentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RankAjaxController {
	
	private final RestaurentService restaurentService;
	
	// button
	@GetMapping("/OJM/button")
	public ResponseEntity<Restaurent> button() {
		ResponseEntity<Restaurent> OJMBtn = this.restaurentService.button();
		return OJMBtn;
	} //
	
} // class
