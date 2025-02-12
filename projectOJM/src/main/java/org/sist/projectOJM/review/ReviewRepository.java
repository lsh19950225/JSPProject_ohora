package org.sist.projectOJM.review;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ReviewRepository extends JpaRepository<Review, Long> {

	// userReview
	List<Review> findByUserNameOrderByWriteDateDesc(String userName);
	
}
