package org.sist.projectOJM.review;

import java.security.Principal;

import org.sist.projectOJM.restaurent.Restaurent;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;
	
	//  let openUrl = "/review?rId="+rId+"&rName="+ displayName + "&rAddr="+address+"&lat="+lat+"&lng="+lng;
	 @GetMapping("/write") 
	 @PreAuthorize("isAuthenticated()")
	public String revWrite( //리뷰 쓰기 띄우기
			@RequestParam("rId") String rId ,
			@RequestParam("rName") String rName ,
			@RequestParam("rAddr") String rAddr ,
			@RequestParam("lat") double lat ,
			@RequestParam("lng") double lng,
			Principal principal
			) {
		 System.out.println("프린씨펄 겟네임00 @@@@@@@@@@ " + principal.getName());
			System.out.println(rId);
			System.out.println(rName);
			System.out.println(rAddr);
			System.out.println(lat);
			System.out.println(lng);

			return "/OJM/revWrite";
		 
		 ///return ""
	 }
	 
	 @PostMapping("/write") 
	 @PreAuthorize("isAuthenticated()")
	public String revWriteInsert( //리뷰 쓰기 띄우기
			@RequestParam("revContent") String revContent,
			@RequestParam("rId") String rId ,
			@RequestParam("rName") String rName ,
			@RequestParam("rAddr") String rAddr ,
			@RequestParam("lat") double lat ,
			@RequestParam("lng") double lng,
			@RequestParam("revRating") int rating,
			@RequestParam("rcate") String cate,
			Principal principal
			) {
		 System.out.println(revContent);
		 System.out.println(rName);
		 System.out.println(rating);
		 System.out.println("프린씨펄 겟네임 @@@@@@@@@@ " + principal.getName());

		 String userName= principal.getName();
		 this.reviewService.reviewInsert(revContent,rId,rName,rAddr,lat,lng,rating,cate,userName);
		 
		 return "redirect:/review/popupClosePage";
		 
		 ///return ""
	 }
	 


		@GetMapping("/popupClosePage")
		@ResponseBody
		public String closePopupAndAlert() {
		    return "<script type='text/javascript'>"
		            + "if (window.opener) {"  // 부모 창이 있는 경우
		            //+ "    window.opener.location.href = '/';"  // 부모 창 이동
		            + "    window.close();"  // 팝업 창 닫기
		            + "} else {"  // 부모 창이 없으면
		            + "    alert('부모 창이 없습니다.');"
		            + "}"
		            + "</script>";
		}

	 
	 
	 
	 
}//class
