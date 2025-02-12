package ohora.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.util.ConnectionProvider;
import com.util.JdbcUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ohora.domain.ProductDTO;
import ohora.domain.UserDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotUserCartDAOImpl implements NotUserCartDAO{
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public NotUserCartDAOImpl(Connection conn) {
		super();
		this.conn = conn;
	}

	@Override
	public ArrayList<ProductDTO> select(int currentPage, int numberPerPage) throws SQLException {

		int pdt_id;
		String pdt_name;
		int pdt_amount;
		int pdt_discount_rate;
		String pdt_img_url;
		int pdt_review_count;
		int pdt_discount_amount;

		ArrayList<ProductDTO> list = null;



		String sql = "SELECT * FROM ( "
				+ "SELECT ROWNUM no, t.* FROM ("
				+ "SELECT pdt_id, pdt_name, pdt_amount, pdt_discount_rate, pdt_img_url, pdt_review_count, pdt_adddate "
				+ "FROM O_PRODUCT "
				// + "ORDER BY pdt_adddate DESC "
				+ ") t "
				+ ") b "
				+ "WHERE no BETWEEN ? AND ? ";

		ProductDTO pdt = null;
		int start = (currentPage-1) * numberPerPage + 1;
		int end = start + numberPerPage -1;
		int totalRecords = getTotalRecords();
		if (end > totalRecords) end = totalRecords;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				list = new ArrayList<ProductDTO>();
				do {
					pdt_id = rs.getInt("pdt_id");
					pdt_name = rs.getString("pdt_name");
					pdt_amount = rs.getInt("pdt_amount");
					pdt_discount_rate = rs.getInt("pdt_discount_rate");
					pdt_img_url = rs.getString("pdt_img_url");
					pdt_review_count = rs.getInt("pdt_review_count");

					if (pdt_discount_rate != 0) {
						pdt_discount_amount = (int)(pdt_amount - (pdt_amount * (pdt_discount_rate / 100.0f)));                  
					} else {
						pdt_discount_amount = pdt_amount;
					}

					pdt = new ProductDTO().builder()
							.pdt_id(pdt_id)
							.pdt_name(pdt_name)
							.pdt_amount(pdt_amount)
							.pdt_discount_rate(pdt_discount_rate)
							.pdt_img_url(pdt_img_url)
							.pdt_review_count(pdt_review_count)
							.pdt_discount_amount(pdt_discount_amount)
							.build();

					list.add(pdt);

				} while (rs.next());

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	@Override
	public int getTotalRecords() throws SQLException {
		int totalRecords = 0;
		String sql = null;
		sql = "SELECT COUNT(*) FROM O_PRODUCT ";

		this.pstmt = this.conn.prepareStatement(sql);
		this.rs =  this.pstmt.executeQuery();
		if( this.rs.next() ) totalRecords = rs.getInt(1);
		this.rs.close();
		this.pstmt.close();
		return totalRecords;
	}

	@Override
	public int getTotalRecords(int categoryNumber) throws SQLException {
		int totalRecords = 0;
		String sql = null;

		sql = "SELECT COUNT(*) FROM O_PRODUCT WHERE CAT_ID = ? ";	

		this.pstmt = this.conn.prepareStatement(sql);
		this.pstmt.setInt(1, categoryNumber);
		this.rs =  this.pstmt.executeQuery();
		if( this.rs.next() ) totalRecords = rs.getInt(1);
		this.rs.close();
		this.pstmt.close();
		return totalRecords;
	}

	@Override
	public int getTotalRecords(String searchCondition, String searchWord) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalPages(int numberPerPage) throws SQLException {
		int totalPages = 0;
		String sql = "SELECT CEIL(COUNT(*)/?) FROM O_PRODUCT";
		this.pstmt = this.conn.prepareStatement(sql);
		this.pstmt.setInt(1, numberPerPage);
		this.rs =  this.pstmt.executeQuery();
		if( this.rs.next() ) totalPages = rs.getInt(1);
		this.rs.close();
		this.pstmt.close();
		return totalPages;
	}

	@Override
	public int getTotalPages(int numberPerPage, String searchCondition, String searchWord) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	/*@Override
	public ArrayList<ProductDTO> select(int currentPage, int numberPerPage) throws SQLException {

		String pdt_name;
		int pdt_amount;
		int pdt_discount_rate;
		String pdt_img_url;
		int pdt_review_count;
		int pdt_discount_amount;

		ArrayList<ProductDTO> list = null;


		String sql = "SELECT * FROM ( "
				+ "SELECT ROWNUM no, t.* FROM ("
				+ "SELECT pdt_name, pdt_amount, pdt_discount_rate, pdt_img_url, pdt_review_count, pdt_adddate "
				+ "FROM O_PRODUCT "
				// + "ORDER BY pdt_adddate DESC "
				+ ") t "
				+ ") b "
				+ "WHERE no BETWEEN ? AND ? ";

		ProductDTO pdt = null;
		int start = (currentPage-1) * numberPerPage + 1;
		int end = start + numberPerPage -1;
		int totalRecords = getTotalRecords();
		if (end > totalRecords) end = totalRecords;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				list = new ArrayList<ProductDTO>();
				do {

					pdt_name = rs.getString("pdt_name");
					pdt_amount = rs.getInt("pdt_amount");
					pdt_discount_rate = rs.getInt("pdt_discount_rate");
					pdt_img_url = rs.getString("pdt_img_url");
					pdt_review_count = rs.getInt("pdt_review_count");

					if (pdt_discount_rate != 0) {
						pdt_discount_amount = pdt_amount - (pdt_amount / pdt_discount_rate);						
					} else {
						pdt_discount_amount = pdt_amount;
					}

					pdt = new ProductDTO().builder()
							.pdt_name(pdt_name)
							.pdt_amount(pdt_amount)
							.pdt_discount_rate(pdt_discount_rate)
							.pdt_img_url(pdt_img_url)
							.pdt_review_count(pdt_review_count)
							.pdt_discount_amount(pdt_discount_amount)
							.build();

					list.add(pdt);

				} while (rs.next());

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}*/

	// 로그인 처리 시작
	@Override
	public int loginNum(String id, String password) throws SQLException {

		String sql = "SELECT user_password, user_id FROM o_user WHERE user_login_id = ?";

		try {

			// 암호화 처리 필요
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
		}
		return rowCount;

	}
	//회원가입(준용) 끝

	//아이디 이메일 전화번호 중복체크(준용)
	@Override
	public int jungbokCK(Connection conn, UserDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		int rowCount = 0;

		String sql = 		" SELECT user_id "
				+ " FROM o_user "
				+ " where user_login_id = '?' OR user_tel = '?' OR user_email = '?'; ";

		return 0;
	}



	@Override
	public ArrayList<ProductDTO> bringProducts(List<Integer> pdtIds) throws SQLException {
	    ArrayList<ProductDTO> list = new ArrayList<>();
	    StringBuilder sql = new StringBuilder();
	    sql.append("SELECT pdt_id, cat_id, scat_id, pdt_number, pdt_name, ")
	       .append("pdt_amount, pdt_discount_rate, pdt_img_url, pdt_count, ")
	       .append("pdt_review_count, pdt_sales_count, pdt_adddate, pdt_viewcount ")
	       .append("FROM O_PRODUCT WHERE pdt_id IN (")
	       .append(String.join(",", Collections.nCopies(pdtIds.size(), "?")))
	       .append(")");
	    
	    try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {
	        for (int i = 0; i < pdtIds.size(); i++) {
	            pstmt.setInt(i + 1, pdtIds.get(i));
	        }
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            while (rs.next()) {
	                int pdtAmount = rs.getInt("pdt_amount");
	                int pdtDiscountRate = rs.getInt("pdt_discount_rate");
	                int pdtDiscountAmount = (pdtDiscountRate != 0) 
	                    ? (int) (pdtAmount - (pdtAmount * (pdtDiscountRate / 100.0f))) 
	                    : pdtAmount;
	                
	                ProductDTO pdt = ProductDTO.builder()
	                    .pdt_id(rs.getInt("pdt_id"))
	                    .pdt_name(rs.getString("pdt_name"))
	                    .pdt_amount(pdtAmount)
	                    .pdt_discount_rate(pdtDiscountRate)
	                    .pdt_discount_amount(pdtDiscountAmount)
	                    .pdt_img_url(rs.getString("pdt_img_url"))
	                    .pdt_count(rs.getInt("pdt_count"))
	                    .build();
	                    
	                list.add(pdt);
	            }
	        }
	    }
	    return list;
	}

	@Override
	public boolean noPdtCountUp(int pdtId, int quantity) {
	    System.out.println("DAOImpl (비회원)장바구니: 상품 수량 증가 - pdtId = " + pdtId);
	    
	    String pdtCountSql = "SELECT pdt_count FROM o_product WHERE pdt_id = ?";
	    String updateProductStockSql = "UPDATE o_product SET pdt_count = pdt_count - 1 WHERE pdt_id = ? AND pdt_count > 0";
	    boolean isUpdated = false;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;

	    try {
	        conn.setAutoCommit(false); 
	        
	        // 현재 재고 확인
	        pstmt = conn.prepareStatement(pdtCountSql);
	        pstmt.setInt(1, pdtId);
	        rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            int currentStock = rs.getInt("pdt_count");
	            int newQuantity = quantity + 1; // 증가하려는 수량
	            
	            System.out.println("현재 재고: " + currentStock);
	            System.out.println("요청 수량: " + newQuantity);
	            
	            // 현재 재고와 요청 수량 비교
	            if (newQuantity <= currentStock + quantity) {  // 현재 재고 + 장바구니 수량까지 허용
	                JdbcUtil.close(pstmt);
	                
	                // 재고 감소
	                pstmt = conn.prepareStatement(updateProductStockSql);
	                pstmt.setInt(1, pdtId);
	                int success = pstmt.executeUpdate();
	                if (success > 0) {
	                    isUpdated = true;
	                    conn.commit();
	                    System.out.println("비회원: 수량 증가 성공");
	                }
	            } else {
	                System.out.println("오류: 재고(" + currentStock + ")보다 많은 수량을 요청했습니다.");
	                conn.rollback();
	            }
	        }
	    } catch (Exception e) {
	        try {
	            conn.rollback();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	        e.printStackTrace();
	    } finally {
	        try {
	            conn.setAutoCommit(true);
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        JdbcUtil.close(rs);
	        JdbcUtil.close(pstmt);
	    }
	    
	    return isUpdated;
	}
    // 비회원 장바구니 상품 수량 증가 끝

	
	@Override
	public boolean noPdtCountDown(int pdtId, int quantity) {
		 System.out.println("DAOImpl (비회원)장바구니: 상품 수량 감소 - pdtId = " + pdtId);
		    
		    String updateProductStockSql = "UPDATE o_product SET pdt_count = pdt_count + 1 WHERE pdt_id = ?";
		    boolean isUpdated = false;

		    try {
		        conn.setAutoCommit(false); // 트랜잭션 시작
		        
		        // 현재 장바구니 수량이 1 이상일 때만 감소 허용
		        if (quantity > 1) {
		            // 재고 증가
		            try (PreparedStatement stockStmt = conn.prepareStatement(updateProductStockSql)) {
		                stockStmt.setInt(1, pdtId);
		                int success = stockStmt.executeUpdate();
		                if (success > 0) {
		                    isUpdated = true;
		                    conn.commit(); // 트랜잭션 커밋
		                    System.out.println("비회원: 수량 감소 성공");
		                }
		            }
		        } else {
		            System.out.println("오류: 장바구니 수량이 1 이하이므로 감소할 수 없습니다.");
		            conn.rollback(); // 실패시 롤백
		        }
		    } catch (Exception e) {
		        try {
		            conn.rollback(); // 에러시 롤백
		        } catch (SQLException ex) {
		            ex.printStackTrace();
		        }
		        e.printStackTrace();
		    } finally {
		        try {
		            conn.setAutoCommit(true); // 트랜잭션 종료
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		        JdbcUtil.close(pstmt);
		        JdbcUtil.close(conn);
		    }
		    
		    return isUpdated;
		}// 비회원 장바구니 상품 수량 감소 끝
	
	//비회원 장바구니 삭제 시작
	@Override
	public boolean deletePdt(int pdtId, int quantity) throws SQLException {
		String sql = "UPDATE o_product SET pdt_count = pdt_count + ? WHERE pdt_id = ?";
	    
	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, quantity);
	        pstmt.setInt(2, pdtId);
	        
	        int affected = pstmt.executeUpdate();
	        return affected > 0;
	    }
	}
}



