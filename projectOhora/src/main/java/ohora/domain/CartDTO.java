package ohora.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDTO {
	
	private String pdt_name; // 상품 이름
	private int pdt_amount; // 상품 가격
	private int clist_pdt_count; // 상품 수량
	private String clist_select; // 상품 체크 여부
	private int pdt_id; // 상품 id
	private String pdt_img_url; // 이미지 경로
	private int pdt_discount_rate; // 할인율
	private int opt_id; // 옵션 id
	private int clist_id; // 장바구니 id
	
} // class
