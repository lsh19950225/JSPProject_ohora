package org.sist.projectOJM.review;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.sist.projectOJM.comment.Comment;
import org.sist.projectOJM.restaurent.Restaurent;
import org.sist.projectOJM.user.SiteUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Review {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reviewId;
	
	private Long userId;
	private String userName;
	
	@Column(length = 1500) // 리뷰내용
	private String content;
	
	private LocalDateTime writeDate; //작성일
	
	@Max(value = 5)
	@Min(value = 1)
	private int rating; //별점
	
	private char isPhoto;
	
	//private String restaurentId; //음식 카테고리
	
	@OneToMany(mappedBy = "review" , cascade = CascadeType.REMOVE , fetch =FetchType.EAGER)
	private List<Comment> commentList;

	@ManyToOne
	private Restaurent restaurent;
	
}
