package ohora.persistence;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import ohora.domain.CartDTO;
import ohora.domain.UserDTO;

public interface UserCartDAO {

	

	// 회원 장바구니 버튼 클릭 시 디비 저장 ajax
	Map<String, Object> addCart(int userId, int pdtId, HttpServletRequest request);

	// 회원 장바구니 select 처리
	ArrayList<CartDTO> selectCartList(Integer userId);

	// 회원 장바구니 삭제
	boolean delCart(int clistId, HttpServletRequest request);

	// 회원 장바구니 상품 수량 증가
	boolean pdtCountUp(int clistId, int pdtId, int optId);

	// 회원 장바구니 상품 수량 감소
	boolean pdtCountDown(int clistId);
}
