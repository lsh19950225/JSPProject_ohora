package org.sist.projectOJM.comment;

import java.time.LocalDateTime;
import java.util.Date;

import org.sist.projectOJM.review.Review;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long cmtId;
	
	@ManyToOne
	private Review review; //리뷰 아이디
	
	private Long userId;
	
	@Column(length = 500) // 댓글내용
	private String cmt_content;
	
	private LocalDateTime writeDate; //작성일
	
	
	
}
