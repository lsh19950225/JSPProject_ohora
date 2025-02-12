package ohora.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPageDTO {
	
	private String user_name; // 회원 이름
	private String mem_name; // 멤버십명
	private int user_point; // 적립금
	private String opdt_state; // 주문처리상태
    private String orderStatus;  // 주문 상태 필드
    private Date ord_orderdate; // 주문일
    private String opdt_name; // 상품명
    private int opdt_amount; // 주문상품 가격
    // 추가
    private int opdt_id;
    private int ord_pk; // pk
    
} // class
