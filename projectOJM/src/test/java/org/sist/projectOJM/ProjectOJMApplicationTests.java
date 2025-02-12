package org.sist.projectOJM;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.sist.projectOJM.restaurent.Restaurent;
import org.sist.projectOJM.restaurent.RestaurentRepository;
import org.sist.projectOJM.review.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ProjectOJMApplicationTests {
	
	@Autowired
	private RestaurentRepository restaurentRepository;
	
	@Transactional
	@Test
	void testOJM() {
		String rId = "ChIJvb8GXAChfDURU8w7NffZITI";
		//double avg = restaurentForInsert.getAvgrating();
		double sum=0;
		Optional<Restaurent> or = restaurentRepository.findByRestaurentId(rId);
	
		if( or.isPresent() ) {
			Restaurent r = or.get();
			List<Review> reviewList = r.getReviewList();
			// reviewList.stream().forEach(a-> a );
			sum = reviewList.stream().mapToInt(Review::getRating).sum();
			double avg = sum/reviewList.size();
			double truncatedAvg = Math.floor(avg * 100.0) / 100.0;
			System.out.println(truncatedAvg);
			//평균별점 넣어주기
		}//if
		System.out.println("zzzz2");
	}
	
	
	@Test
	void testOJM2() {
	
	List<Restaurent> allRestaurent = this.restaurentRepository.findAllByOrderByAvgratingDesc();;
	for (Restaurent r : allRestaurent) {
		System.out.println( r.getRname());
	}
	
	
	}
	
	
	
} // class
