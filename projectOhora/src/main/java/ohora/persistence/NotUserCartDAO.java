package ohora.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.util.ConnectionProvider;

import ohora.domain.DeptVO;
import ohora.domain.ProductDTO;
import ohora.domain.UserDTO;

public interface NotUserCartDAO {
	ArrayList<ProductDTO> select(int currentPage, int numberPerPage) throws SQLException;
	
	
	// 1-3. 총 레코드 수
	int getTotalRecords() throws SQLException;
	
	int getTotalRecords(int categoryNumber) throws SQLException;
	
	int getTotalRecords(String searchCondition, String searchWord) throws SQLException;
	// 1-4. 총 페이지 수
	int getTotalPages(int numberPerPage) throws SQLException;
	// 검색시 총페이지
	int getTotalPages(int numberPerPage, String searchCondition, String searchWord) throws SQLException;
	
	
	//회원 가입

	int insert(Connection conn, UserDTO dto) throws SQLException;

	//아이디 이메일 폰번호 중복체크
	int jungbokCK( Connection conn, UserDTO dto ) throws SQLException;
	
	
	//로그인
	int loginNum(String id, String password) throws SQLException;
	
	
	//상품 아이디로 정보 가져오기
	ArrayList<ProductDTO> bringProducts(List<Integer> pdtIds) throws SQLException;
	
	// 비회원장바구니 상품 수량 증가
	boolean noPdtCountUp(int pdtId, int quantity);
	   
    // 비회원 장바구니 상품 수량 감소
	boolean noPdtCountDown(int pdtId, int quantity);
	
	// 비회원 장바구니 삭제
	boolean deletePdt(int pdtId, int quantity) throws SQLException;
}
