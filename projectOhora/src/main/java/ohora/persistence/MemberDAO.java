package ohora.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import ohora.domain.AddrDTO;
import ohora.domain.AddressDTO;
import ohora.domain.UserDTO;

public interface MemberDAO {
	//회원 가입 (준용)
	int insert(Connection conn, UserDTO dto) throws SQLException;

	//회원가입 페이지 아이디 이메일 폰번호 중복체크(준용)
	boolean jungbokCK( Connection conn, UserDTO dto ) throws SQLException;



	// 로그인 처리 (시훈)
	int loginNum(String id, String password) throws SQLException;

	// 아이디 찾기(시훈)
	String findUser(UserDTO user);

	// 비밀번호 찾기(시훈)
	boolean findPasswd(UserDTO userFindPasswd);

	// 비밀번호 변경(시훈)
	int changePasswd(String confirmPassword, String userID);

	// 로그인 시 장바구니 count 저장 처리(시훈)
	int cartCount(int dbUserId);
	
	// 회원정보 출력 (재윤)
	UserDTO selectUserInfo(Connection conn, int userId) throws 	SQLException;
	
	// 회원 정보 수정(세호)
	int updateUser(Connection conn, UserDTO dto) throws SQLException, ParseException;

	//배송지 등록 (준용)
	int insertAddr(Connection conn, AddressDTO dto) throws SQLException, ParseException;

	


	
	// 멤버 개인정보 수정 (재윤)
	//UserDTO selectUserInfo(Connection conn, int userId) throws SQLException;
}
