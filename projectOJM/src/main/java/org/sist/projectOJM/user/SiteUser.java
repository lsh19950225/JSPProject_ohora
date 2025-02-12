package org.sist.projectOJM.user;

import java.util.List;

import org.sist.projectOJM.review.Review;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

//회원 Entity
@Entity
@Getter
@Setter
public class SiteUser {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY) // 시퀀스임
   private Long userId;
   
   @Column(unique=true)
   private String userLoginId;
   
   private String userName;
   
   //다른 속성 안줄거면 @Column 안줘도 똑같음
   private String userPassword1;
   
   @Column(unique=true)
   private String userEmail;
   
}

