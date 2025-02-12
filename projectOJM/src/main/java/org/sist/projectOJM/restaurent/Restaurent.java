package org.sist.projectOJM.restaurent;

import java.util.List;

import org.sist.projectOJM.review.Review;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@JsonIgnoreType // 추가
public class Restaurent {
	
	
	@Id
	private String restaurentId;
	
	@Column(length = 50) // 이름
	private String  rname;
	
	@Column(length = 100) // 주소
	private String raddr;
	
	private double lat; //위도
	
	private double lng; //경도
	
	private double avgrating; //평균 별점
	
	private String rcategory; //음식 카테고리
	
	@OneToMany(mappedBy = "restaurent" , cascade = CascadeType.REMOVE , fetch =FetchType.EAGER)
	private List<Review> reviewList;
	
}
