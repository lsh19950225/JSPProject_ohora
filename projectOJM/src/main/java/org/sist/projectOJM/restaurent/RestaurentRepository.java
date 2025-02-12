package org.sist.projectOJM.restaurent;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurentRepository extends JpaRepository<Restaurent, String> {
	
		// top10
		List<Restaurent> findTop10ByOrderByAvgratingDesc();
		
		List<Restaurent> findByOrderByAvgratingDesc(Pageable pageable);
		
		// all
		List<Restaurent> findAllByOrderByAvgratingDesc();
		Page<Restaurent> findAll(Pageable pageable);
		
		// search
		Page<Restaurent> findByRnameContaining(String search, Pageable pageable);
		
		// main
		Page<Restaurent> findByRcategoryOrderByAvgratingDesc(String category, Pageable pageable);

	
	//이미 있는 식당인지 검색 - 준용
	int countByRestaurentId(String restaurentId);

	Optional<Restaurent> findByRestaurentId(String rId);

	
	
	
	
} // interface
