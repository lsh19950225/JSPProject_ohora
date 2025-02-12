package org.sist.projectOJM.restaurent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurentForm {
	
	private String restaurentId; // String 수정 필요
	private String rname;
	private String raddr;
	private double lat;
	private double lng;
	private double avgrating;
	private String rcategory;
	
} // class
