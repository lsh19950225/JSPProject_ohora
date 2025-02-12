package ohora.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import com.util.ConnectionProvider;
import com.util.JdbcUtil;

import ohora.domain.AddrDTO;
import ohora.domain.AddressDTO;
import ohora.domain.ReviewDTO;
import ohora.domain.UserDTO;

public class MemberDAOImpl implements MemberDAO {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public MemberDAOImpl(Connection conn) {
		super();
		this.conn = conn;
	}




	//회원가입 (준용) DAOimpl
	@Override
	public int insert(Connection con, UserDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		int rowCount = 0;

		String sql = 

				" INSERT INTO o_user "
						+	" ( user_id, mem_id, auth_id, user_login_id, user_password, user_name, user_email, user_tel, user_birth, user_point, user_smsagree, user_joindate, user_emailagree ) "
						+ " VALUES ( O_USER_SEQ.NEXTVAL , ? , ? , ? , ? , ?, ? , ?, ? ,? ,? ,? ,? ) " ;

		try {
			pstmt = con.prepareStatement(sql);

			pstmt.setLong(1, dto.getMem_id());
			pstmt.setLong(2, dto.getAuth_id());
			pstmt.setString(3, dto.getUser_login_id());
			pstmt.setString(4, dto.getUser_password());
			pstmt.setString(5, dto.getUser_name());
			pstmt.setString(6, dto.getUser_email());
			pstmt.setString(7, dto.getUser_tel());
			pstmt.setDate(8, new java.sql.Date(dto.getUser_birth().getTime())); 
			pstmt.setLong(9, dto.getUser_point());
			pstmt.setString(10, String.valueOf(dto.getUser_smsAgree()));
			pstmt.setDate(11, new java.sql.Date(dto.getUser_joindate().getTime())); 
			pstmt.setString(12, String.valueOf(dto.getUser_emailAgree()));

			rowCount = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("캣치 잡혔다 회원가입");
			e.printStackTrace(); 
		} finally {
			JdbcUtil.close(pstmt);
			pstmt.close();
		}
		return rowCount;

	}
	//회원가입(준용) 끝

	//아이디 이메일 전화번호 중복체크(준용)
	@Override
	public boolean jungbokCK(Connection con, UserDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		int rowCount = 0;
		boolean isjungbok = false;

		System.out.println("DAOImpl 진입완료");
		System.out.println(dto.getUser_login_id());


		String sql = 		" SELECT user_id "
				+ " FROM o_user "
				+ " where user_login_id = ? OR user_tel = ? OR user_email = ? ";
		try {
			pstmt = con.prepareStatement(sql);

			if( dto.getUser_login_id() != null ) { 
				//System.out.println("아이디 ? 채우기 진입 완료");
				pstmt.setString(1, dto.getUser_login_id());
				pstmt.setString(2, dto.getUser_login_id());
				pstmt.setString(3, dto.getUser_login_id());
			} else  if( dto.getUser_tel( ) != null ) {
				//System.out.println("전화번호 ? 채우기 진입 완료");
				pstmt.setString(1, dto.getUser_tel());
				pstmt.setString(2, dto.getUser_tel());
				pstmt.setString(3, dto.getUser_tel());
			} else  if ( dto.getUser_email() != null) {
				//System.out.println("이메일 ? 채우기 진입 완료");
				pstmt.setString(1, dto.getUser_email());
				pstmt.setString(2, dto.getUser_email());
				pstmt.setString(3, dto.getUser_email());
			}

			rowCount = pstmt.executeUpdate();
			System.out.println("로우카운트:" + rowCount);
			isjungbok = rowCount >= 1 ? true : false;

		} catch (Exception e) {
			System.out.println("캣치 잡혔다 중복체크");
			e.printStackTrace(); 
		} finally {
			JdbcUtil.close(pstmt);
			pstmt.close();
		}

		return isjungbok;
	}







	// 로그인 처리 시작
	@Override
	public int loginNum(String id, String password) throws SQLException {

		String sql = "SELECT user_password, user_id FROM o_user WHERE user_login_id = ?";

		System.out.println("dao : " + id);
		System.out.println("dao : " + password);

		try {

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				String dbPassword = rs.getString("user_password");
				int dbUserId = rs.getInt("user_id");

				if(dbPassword.equals(password)) {
					return dbUserId;
				} // if
			} // if

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rs.close();
			pstmt.close();
		}

		return -1;

	}
	// 로그인 처리 끝




	//비번 찾기 아이디찾기 (시훈)
	// 아이디 찾기 시작
	@Override
	public String findUser(UserDTO user) {

		String sql;
		String name = user.getUser_name();
		String email = user.getUser_email();
		String phone = user.getUser_tel();

		System.out.println("dao name : " + name + "\n");
		System.out.println("dao email : " + email + "\n");
		System.out.println("dao phone : " + phone + "\n");

		try {

			if (phone.equals("")) {
				sql = "SELECT user_login_id, user_name, user_email FROM o_user WHERE user_name = ? AND user_email = ?";
			} else {
				sql = "SELECT user_login_id, user_name, user_tel FROM o_user WHERE user_name = ? AND user_tel = ?";
			} // if else

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, name);

			if (phone.equals("")) {
				pstmt.setString(2, email);
			} else {
				pstmt.setString(2, phone);
			} // if else

			rs = pstmt.executeQuery();

			if (rs.next()) {

				String dbUserLoginId = rs.getString("user_login_id");
				return dbUserLoginId;

				/*
							if( phone == null ) {
								String dbUserEmail = rs.getString("user_email");
								if (email.equals(dbUserEmail)) {
									return dbUserLoginId;
								} // if
							} else {
								String dbUserPhone = rs.getString("user_tel");
								if (phone.equals(dbUserPhone)) {
									return dbUserLoginId;
								} // if
							} // if else
				 */

			} // if

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			} // try catch

		} // try catch finally

		return null;

	}
	// 아이디 찾기 끝

	// 비밀번호 찾기 시작
	@Override
	public boolean findPasswd(UserDTO userFindPasswd) {

		String sql;
		String userID = userFindPasswd.getUser_login_id();
		String name = userFindPasswd.getUser_name();
		String email = userFindPasswd.getUser_email();
		String phone = userFindPasswd.getUser_tel();

		// 값 출력 test
		System.out.println("dao userID : " + userID);
		System.out.println("dao name : " + name);
		System.out.println("dao email : " + email);
		System.out.println("dao phone : " + phone);

		try {

			if (phone == null) {
				sql = " SELECT user_password ";
				sql += " FROM o_user ";
				sql += " WHERE user_login_id = ? AND user_name = ? AND user_email = ? ";
			} else {
				sql = " SELECT user_password ";
				sql += " FROM o_user ";
				sql += " WHERE user_login_id = ? AND user_name = ? AND user_tel = ? ";
			} // if else

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, userID);
			pstmt.setString(2, name);

			if (phone == null) {
				pstmt.setString(3, email);
			} else {
				pstmt.setString(3, phone);
			} // if else

			rs = pstmt.executeQuery();

			if (rs.next()) {

				return true;

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) rs.close();
				if (pstmt != null) pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			} // try catch
		} // try catch finally


		return false;

	}
	// 비밀번호 찾기 끝

	// 비밀번호 변경 시작
	@Override
	public int changePasswd(String confirmPassword, String userID) {

		System.out.println("dao password : " + confirmPassword);
		System.out.println("dao id : " + userID);

		String sql;

		try {

			sql = " update o_user ";
			sql += " set user_password = ? ";
			sql += " where user_login_id = ? ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, confirmPassword);
			pstmt.setString(2, userID);

			int num = pstmt.executeUpdate();

			return num;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			} // try catch
		} // try catch finally

		return -1;

	}
	// 비밀번호 변경 끝


	// 로그인 시 장바구니 count 처리 시작
	@Override
	public int cartCount(int dbUserId) {

		System.out.println("dao userId : " + dbUserId);

		String productCountSql = "SELECT COUNT(DISTINCT pdt_id) AS product_count FROM o_cartlist WHERE user_id = ? AND clist_select = 'Y'";

		int productCount = 0;

		try (Connection conn = ConnectionProvider.getConnection();
				PreparedStatement productCountStmt = conn.prepareStatement(productCountSql)) {

			// 5. 유저의 총 상품 수 가져오기 (겹치는 상품과 옵션은 하나로 처리)
			productCountStmt.setInt(1, dbUserId);
			ResultSet countRs = productCountStmt.executeQuery();
			if (countRs.next()) {
				productCount = countRs.getInt("product_count");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} // try catch

		return productCount;
	}
	// 로그인 시 장바구니 count 처리 끝








	/*
	 * //멤버 개인정보 수정
	 * 
	 * @Override public UserDTO selectUserInfo(Connection con, int userId) throws
	 * SQLException { PreparedStatement pstmt = null; int rowCount = 0;
	 * 
	 * 
	 * UserDTO user = null;
	 * 
	 * String sql = " select * " + " from o_user " + " WHERE user_id= ? ";
	 * 
	 * try { pstmt = con.prepareStatement(sql);
	 * 
	 * pstmt.setLong(1, userId);
	 * 
	 * rs = pstmt.executeQuery();
	 * 
	 * if ( rs.next()) { user = new UserDTO().builder()
	 * .user_id(rs.getInt("user_id")) .user_login_id(rs.getString("user_login_id"))
	 * .user_name(rs.getString("user_name")) .user_email(rs.getString("user_email"))
	 * .user_tel(rs.getString("user_tel"))
	 * .rev_good_count(rs.getInt("rev_good_count"))
	 * .rev_bad_count(rs.getInt("rev_bad_count"))
	 * .rev_comment_count(rs.getInt("rev_comment_count"))
	 * .rev_isrecommend(rs.getString("rev_isrecommend") != null ?
	 * rs.getString("rev_isrecommend") : "N" )
	 * .rev_isphoto(rs.getString("rev_isphoto")!= null ? rs.getString("rev_isphoto")
	 * : "N") .rev_age_group(rs.getString("rev_age_group")!= null ?
	 * rs.getString("rev_age_group") : "모름")
	 * .rev_option(rs.getString("rev_option")!= null ? rs.getString("rev_option") :
	 * "없음") .pdt_id(rs.getInt("pdt_id")) .user_name(rs.getString("user_name"))
	 * .newImg( new Boolean( rs.getString("new") ) )
	 * //.mediacount(rs.getInt("mediacount") ) .build();
	 * 
	 * }
	 * 
	 * } catch (Exception e) { System.out.println("캣치 잡혔다 회원가입");
	 * e.printStackTrace(); } finally { JdbcUtil.close(pstmt); pstmt.close(); }
	 * 
	 * 
	 * return user;
	 * 
	 * }
	 */

	
	// 재윤
    //멤버 기본정보 불러오기
   @Override
	public UserDTO selectUserInfo(Connection conn, int userId) throws SQLException {
		PreparedStatement pstmt = null;
		int rowCount = 0;
		
		
		UserDTO user = null;
		 
		String sql = " select * "
					+ " from o_user "
					+ " WHERE user_id= ? ";

		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setLong(1, userId);

			rs = pstmt.executeQuery();
			
			if ( rs.next()) {
				user = new UserDTO().builder()
						.user_id(rs.getInt("user_id"))
						.user_login_id(rs.getString("user_login_id"))
						.user_name(rs.getString("user_name"))
						.user_tel(rs.getString("user_tel"))
					    .user_smsAgree(rs.getString("user_smsAgree") != null ? rs.getString("user_smsAgree").charAt(0) : 'N')  // char로 변환							
						.user_email(rs.getString("user_email"))
						.user_emailAgree(rs.getString("user_emailAgree") != null ? rs.getString("user_emailAgree").charAt(0) : 'N') // char로 변환
						.user_birth(rs.getDate("user_birth"))
						.user_password(rs.getString("user_password"))
						.build();

			}

		} catch (Exception e) {
			System.out.println("회원정보 출력");
			e.printStackTrace(); 
		} finally {
			JdbcUtil.close(pstmt);
			pstmt.close();
		}
		
		
		return user;
		
	}			
	
	
	
	// 세호
	// 회원 정보 수정
	public int updateUser(Connection conn, UserDTO dto) throws SQLException, ParseException {
		String sql = "UPDATE o_user "
				+ "SET USER_PASSWORD = COALESCE(?, USER_PASSWORD), "
				+ "USER_EMAIL = ?, USER_TEL = ?, USER_BIRTH = ?, "
				+ "USER_SMSAGREE = ?, USER_EMAILAGREE = ?, USER_NAME = ? "
				+ "WHERE USER_LOGIN_ID = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, dto.getUser_password()); // Null일 경우 COALESCE로 기존 값 유지
			pstmt.setString(2, dto.getUser_email());
			pstmt.setString(3, dto.getUser_tel());
			pstmt.setDate(4, new java.sql.Date(dto.getUser_birth().getTime()));
			pstmt.setString(5, String.valueOf(dto.getUser_smsAgree()));
			pstmt.setString(6, String.valueOf(dto.getUser_emailAgree()));
			pstmt.setString(7, dto.getUser_name());
			pstmt.setString(8, dto.getUser_login_id());

			return pstmt.executeUpdate();
		}
	}




	@Override
	public int insertAddr(Connection conn, AddressDTO dto) throws SQLException, ParseException {
		 
		String sql = "INSERT INTO o_address "
						+ "VALUES ( o_addr_seq.nextval , ?, ?, ?, '' , ?, ?, ?, ?, 'N') ";
		
		int rowCount=0;
		
		try {
			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, dto.getUser_id());
			pstmt.setString(2, dto.getAddr_nick());
			pstmt.setString(3, dto.getAddr_name());
			pstmt.setString(4, dto.getAddr_tel());
			pstmt.setString(5, dto.getAddr_address_main());
			pstmt.setString(6, dto.getAddr_address_detail());
			pstmt.setString(7, dto.getAddr_zipcode());
		
			rowCount = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("캣치 잡혔다 배송지등록");
			e.printStackTrace(); 
		} finally {
			JdbcUtil.close(pstmt);
			pstmt.close();
		}
		return rowCount;


	}

}













