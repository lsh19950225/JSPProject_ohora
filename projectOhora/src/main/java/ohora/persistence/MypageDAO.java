package ohora.persistence;

import java.util.ArrayList;

import ohora.domain.MyPageDTO;
import ohora.domain.orderDetailDTO;

public interface MypageDAO {
	// 마이페이지 이름
	ArrayList<MyPageDTO> myPageName(Integer userId);

	// 마이페이지 order list
	ArrayList<MyPageDTO> orderList(Integer userId);

	// 마이페이지 주문 부분
	ArrayList<MyPageDTO> ordDetail(Integer userId);
	
	// 주문정보, 결제정보 select 처리
	ArrayList<orderDetailDTO> selectOrderDetail(int ord_pk, Integer userId);
	
	// 주문 상품 정보 select 처리
	ArrayList<orderDetailDTO> selectOrderList(int ord_pk, Integer userId, String opdt_name, int opdt_id);
	
	// 배송지정보 부분
	ArrayList<orderDetailDTO> selectAddrList(Integer userId);
}
