package org.sist.projectOJM.restaurent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurentService {
	
	private final RestaurentRepository restaurentRepository;
	
	// top10
	public List<Restaurent> topList() {
		return this.restaurentRepository.findTop10ByOrderByAvgratingDesc();
	} //
	

	/*
	// all
	public List<Restaurent> allList() {
		return this.restaurentRepository.findAllByOrderByAvgratingDesc();
	} //
	*/
	
	public Page<Restaurent> allList(int page) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("avgrating")); // 별점순 정렬
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return this.restaurentRepository.findAll(pageable);
	} //
	
	// search
	public Page<Restaurent> searchList(String search, int page) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("avgrating")); // 별점순 정렬
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return this.restaurentRepository.findByRnameContaining(search, pageable);
	} //
	
	// korea
	public Page<Restaurent> koreaList(String category, int page) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("avgrating")); // 별점순 정렬
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return this.restaurentRepository.findByRcategoryOrderByAvgratingDesc(category, pageable);
	} //
	
	// china
	public Page<Restaurent> chinaList(String category, int page) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("avgrating")); // 별점순 정렬
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return this.restaurentRepository.findByRcategoryOrderByAvgratingDesc(category, pageable);
	} //
	
	// america
	public Page<Restaurent> americaList(String category, int page) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("avgrating")); // 별점순 정렬
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return this.restaurentRepository.findByRcategoryOrderByAvgratingDesc(category, pageable);
	} //
	
	// japan
	public Page<Restaurent> japanList(String category, int page) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("avgrating")); // 별점순 정렬
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return this.restaurentRepository.findByRcategoryOrderByAvgratingDesc(category, pageable);
	} //
	
	// fastFood
	public Page<Restaurent> fastFoodList(String category, int page) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("avgrating")); // 별점순 정렬
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		return this.restaurentRepository.findByRcategoryOrderByAvgratingDesc(category, pageable);
	} //
	
	// OJMBtn
	public ResponseEntity<Restaurent> button() {
		List<Restaurent> allRestaurent = this.restaurentRepository.findAll();
		
		// 별점 가중치 계산 후 랜덤 선택
		double totalWeight = allRestaurent.stream()
												.mapToDouble(Restaurent::getAvgrating)
												.sum();
		double random = Math.random() * totalWeight;
		
		double cumulativeWeight = 0.0;
		for (Restaurent restaurent : allRestaurent) {
			cumulativeWeight += restaurent.getAvgrating();
			if (random <= cumulativeWeight) {
				return ResponseEntity.ok(restaurent);
			} // if
		} // for
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
		// reviewBtn
		public Optional<Restaurent> reviewBtn(String restaurentId) {
			Optional<Restaurent> reviewBtn = this.restaurentRepository.findByRestaurentId(restaurentId);
			
			return reviewBtn;
		} //
		
		
		
		
	// 탑10
	public List<Restaurent> getTop10() {
		List<Restaurent> allRestaurent = this.restaurentRepository.findTop10ByOrderByAvgratingDesc();;
		
		return allRestaurent;
	}


	
} // class
