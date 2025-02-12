package org.sist.projectOJM.review;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.sist.projectOJM.restaurent.Restaurent;
import org.sist.projectOJM.restaurent.RestaurentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReviewService {

	private final ReviewRepository reviewRepository; // 주입
	private final RestaurentRepository restaurentRepository; // 주입

	@Transactional
	   public void reviewInsert(String revContent, String rId, String rName, String rAddr, double lat, double lng, int rating,String cate
			   					, String userName) {
	       Review review = new Review();
	       review.setContent(revContent);
	       review.setWriteDate(LocalDateTime.now());
	       review.setRating(rating);
	       review.setIsPhoto('N');  // 포토리뷰는 N으로 설정
	       review.setUserName(userName);
	       
	       Restaurent restaurentForInsert = new Restaurent();
	       restaurentForInsert.setRestaurentId(rId);
	       restaurentForInsert.setRname(rName);
	       restaurentForInsert.setRaddr(rAddr);
	       restaurentForInsert.setLat(lat);
	       restaurentForInsert.setLng(lng);
	       restaurentForInsert.setRcategory(cate);

	       review.setRestaurent(restaurentForInsert);  // 레스토랑Id를 리뷰에 연결

	       // 레스토랑이 이미 존재하는지 확인
	       Optional<Restaurent> or = restaurentRepository.findByRestaurentId(rId);
	       if (or.isPresent()) {
	           Restaurent r = or.get();
	           List<Review> reviewList = r.getReviewList();
	           reviewList.add(review);  // 리뷰를 추가합니다.

	           // 평균 별점 계산
	           double sum = reviewList.stream().mapToInt(Review::getRating).sum();
	           double avg = sum / reviewList.size();
	           double truncatedAvg = Math.floor(avg * 100.0) / 100.0;  // 소수점 둘째 자리로 절삭
	           r.setAvgrating(truncatedAvg);  // 평균 별점 업데이트

	           restaurentRepository.save(r);  // 업데이트된 레스토랑 정보 저장
	           this.reviewRepository.save(review);  // 리뷰 저장
	       } else {
	           // 레스토랑이 없다면 새로 추가
	           restaurentForInsert.setAvgrating(rating);
	           restaurentRepository.save(restaurentForInsert);  // 레스토랑 정보 저장
	           this.reviewRepository.save(review);  // 리뷰 저장
	       }
	   }

	
	   // userReview
	   public List<Review> userReview(String userName) {
	      return this.reviewRepository.findByUserNameOrderByWriteDateDesc(userName);
	   }
	   
	   // deleteBtn
	   @Transactional
	   public void deleteBtn(Long reviewId , String restaurentId, Long restaurentReviews) {
		   if (restaurentReviews == 1) {
				this.reviewRepository.deleteById(reviewId);
				this.restaurentRepository.deleteById(restaurentId);
		   } else { // 0개 아닐 때
			   this.reviewRepository.deleteById(reviewId);
		        Optional<Restaurent> or = restaurentRepository.findById(restaurentId);
			       if (or.isPresent()) {
			           Restaurent r = or.get();
			           List<Review> reviewList = r.getReviewList();
			           
			           // 평균 별점 계산
			           double sum = reviewList.stream().mapToInt(Review::getRating).sum();
			           double avg = sum / reviewList.size();
			           double truncatedAvg = Math.floor(avg * 100.0) / 100.0;  // 소수점 둘째 자리로 절삭
			           r.setAvgrating(truncatedAvg);  // 평균 별점 업데이트
			           
			           System.out.println("@@@@@@@@@@@@@" + truncatedAvg);

			           restaurentRepository.save(r);  // 업데이트된 레스토랑 정보 저장
			       }
			       
		   } // if else
	   } //
	
	   
} // class
