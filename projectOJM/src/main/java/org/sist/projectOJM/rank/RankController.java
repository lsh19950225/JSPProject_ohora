package org.sist.projectOJM.rank;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.sist.projectOJM.page.Criteria;
import org.sist.projectOJM.page.PageDTO;
import org.sist.projectOJM.restaurent.Restaurent;
import org.sist.projectOJM.restaurent.RestaurentRepository;
import org.sist.projectOJM.restaurent.RestaurentService;
import org.sist.projectOJM.review.Review;
import org.sist.projectOJM.review.ReviewRepository;
import org.sist.projectOJM.review.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/rank")
public class RankController {
	
	private final RestaurentService restaurentService;
	private final ReviewService reviewService;
	private final RestaurentRepository restaurentRepository;
	private final ReviewRepository reviewRepository;
	// main
	/*
	@GetMapping("/main")
	public String main(Model model
			, @RequestParam("category") String category) {
		if (category.equals("top10")) { // top10
			List<Restaurent> topList = this.restaurentService.topList();
			model.addAttribute("topList", topList);
			model.addAttribute("activeTab", "top10");
			model.addAttribute("isTop10", true);
			return "/OJM/rank_page";
		} else if (category.equals("all")) { // all
			List<Restaurent> allList = this.restaurentService.allList();
			model.addAttribute("allList", allList);
			model.addAttribute("activeTab", "all");
			model.addAttribute("isTop10", false);
			return "/OJM/rank_page";
		} // if
		return "/OJM/rank_page";
	} //
	*/
	
	// main
	@GetMapping("/main")
	public String main(Model model
			, @RequestParam("category") String category
			, @RequestParam(value = "page", defaultValue = "0") int page) {
		if (category.equals("top10")) { // top10
			List<Restaurent> topList = this.restaurentService.topList();
			model.addAttribute("topList", topList);
			model.addAttribute("activeTab", "top10");
			model.addAttribute("isTop10", true);
			return "/OJM/rank_page";
		}
		else if (category.equals("all")) { // all
			Page<Restaurent> paging = this.restaurentService.allList(page);
			model.addAttribute("paging", paging); // 담기
			model.addAttribute("activeTab", "all");
			model.addAttribute("isTop10", false);
			Criteria criteria = new Criteria(page+1, 10);
			int total = (int)paging.getTotalElements();
			model.addAttribute("pageMaker", new PageDTO(criteria, total));
			return "/OJM/rank_page";
		}
		else if (category.equals("korea")) { // korea
			System.out.println("korea : " + category);
			category = "한식";
			System.out.println("korea : " + category);
			Page<Restaurent> paging = this.restaurentService.koreaList(category, page);
			model.addAttribute("paging", paging); // 담기
			model.addAttribute("activeTab", "korea");
			model.addAttribute("isTop10", false);
			Criteria criteria = new Criteria(page+1, 10);
			int total = (int)paging.getTotalElements();
			model.addAttribute("pageMaker", new PageDTO(criteria, total));
			return "/OJM/rank_page";
		}
		else if (category.equals("china")) { // china
			System.out.println("china : " + category);
			category = "중식";
			System.out.println("china : " + category);
			Page<Restaurent> paging = this.restaurentService.chinaList(category, page);
			model.addAttribute("paging", paging); // 담기
			model.addAttribute("activeTab", "china");
			model.addAttribute("isTop10", false);
			Criteria criteria = new Criteria(page+1, 10);
			int total = (int)paging.getTotalElements();
			model.addAttribute("pageMaker", new PageDTO(criteria, total));
			return "/OJM/rank_page";
		}
		else if (category.equals("america")) { // america
			System.out.println("america : " + category);
			category = "양식";
			System.out.println("america : " + category);
			Page<Restaurent> paging = this.restaurentService.americaList(category, page);
			model.addAttribute("paging", paging); // 담기
			model.addAttribute("activeTab", "america");
			model.addAttribute("isTop10", false);
			Criteria criteria = new Criteria(page+1, 10);
			int total = (int)paging.getTotalElements();
			model.addAttribute("pageMaker", new PageDTO(criteria, total));
			return "/OJM/rank_page";
		}
		else if (category.equals("japan")) { // japan
			System.out.println("japan : " + category);
			category = "일식";
			System.out.println("japan : " + category);
			Page<Restaurent> paging = this.restaurentService.americaList(category, page);
			model.addAttribute("paging", paging); // 담기
			model.addAttribute("activeTab", "japan");
			model.addAttribute("isTop10", false);
			Criteria criteria = new Criteria(page+1, 10);
			int total = (int)paging.getTotalElements();
			model.addAttribute("pageMaker", new PageDTO(criteria, total));
			return "/OJM/rank_page";
		}
		else if (category.equals("fastFood")) { // fastFood
			System.out.println("fastFood : " + category);
			category = "패스트푸드";
			System.out.println("fastFood : " + category);
			Page<Restaurent> paging = this.restaurentService.fastFoodList(category, page);
			model.addAttribute("paging", paging); // 담기
			model.addAttribute("activeTab", "fastFood");
			model.addAttribute("isTop10", false);
			Criteria criteria = new Criteria(page+1, 10);
			int total = (int)paging.getTotalElements();
			model.addAttribute("pageMaker", new PageDTO(criteria, total));
			return "/OJM/rank_page";
		} // if
		return "/OJM/rank_page";
	} //
	
	// search
	@GetMapping("/search")
	public String search(Model model
			, @RequestParam("search") String search
			, @RequestParam(value = "page", defaultValue = "0") int page
			) {
		System.out.println(search);
		// List<Restaurent> searchList = this.restaurentService.searchList(search);
		Page<Restaurent> paging = this.restaurentService.searchList(search, page);
		model.addAttribute("paging", paging);
		model.addAttribute("activeTab", "search");
		model.addAttribute("isTop10", false);
		model.addAttribute("search", search);
		Criteria criteria = new Criteria(page+1, 10);
		int total = (int)paging.getTotalElements();
		model.addAttribute("pageMaker", new PageDTO(criteria, total));
		return "/OJM/rank_page";
	} //
	
	// reviewBtn
    @GetMapping("/reviewBtn")
    public String reviewBtn(@RequestParam("restaurentId") String restaurentId, Model model) {
        System.out.println("@@@@@@@@@@@@@" + restaurentId);
        Optional<Restaurent> reviewOp = this.restaurentService.reviewBtn(restaurentId);
        System.out.println("@@@@@@@@@@@" + reviewOp);
        if (reviewOp.isPresent()) {
          Restaurent restaurent = reviewOp.get();
          
          Map<Integer, Long> ratingCounts = restaurent.getReviewList().stream()
                   .collect(Collectors.groupingBy(Review::getRating, Collectors.counting()));

           // 별점이 없는 경우 0으로 초기화
           for (int i = 1; i <= 5; i++) {
               ratingCounts.putIfAbsent(i, 0L);
           }

           model.addAttribute("ratingCounts", ratingCounts);
           
          System.out.println("@@@@@@@@@@@" + restaurent);
          model.addAttribute("restaurent", restaurent);
          return "/OJM/review";
       } else {
          System.out.println("레스토랑 리뷰를 찾을 수 없습니다.");
          model.addAttribute("errorMessage", "등록된 리뷰가 없습니다.");
          return "redirect:/index?error=true";
       }
    }
	
    
    // userReview
    @GetMapping("/userReview")
    public String userReview(Principal principal, Model model) {
       String userName = principal.getName();
       System.out.println( "@@@@@@@@@@@" + principal.getName() );
       List<Review> userReview = this.reviewService.userReview(userName);
       model.addAttribute("userReview", userReview);
       return "/OJM/userReview";
    } //
    
    // deleteBtn
    @GetMapping("/deleteBtn")
    public String deleteBtn(Model model, @RequestParam("reviewId") Long reviewId
    		, @RequestParam("restaurentId") String restaurentId
    		, @RequestParam("restaurentReviews") Long restaurentReviews) {
        System.out.println("restaurentId @@@@@@@@@@" + restaurentId);
        this.reviewService.deleteBtn(reviewId, restaurentId, restaurentReviews);
	    return "redirect:/rank/userReview";
    }
    
} // class
