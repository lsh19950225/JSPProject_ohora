package ohora.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import com.util.ConnectionProvider;

import ohora.domain.CartDTO;
import ohora.domain.UserDTO;

public class UserCartDAOImpl implements UserCartDAO{
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public UserCartDAOImpl(Connection conn) {
		super();
		this.conn = conn;
	}

		  	// 회원 장바구니 버튼 클릭 시 디비 저장 시작 (시훈)
		  	@Override
		  	public Map<String, Object> addCart(int userId, int pdtId, HttpServletRequest request) {
		  		
		  	    System.out.println("dao userId : " + userId);
		  	    System.out.println("dao pdtId : " + pdtId);
		  	    
		  	    String optIdSql = "SELECT b.opt_id FROM o_product a INNER JOIN o_pdtoption b ON a.pdt_id = b.pdt_id WHERE a.pdt_id = ?";
		  	    String checkSql = "SELECT clist_pdt_count FROM o_cartlist WHERE user_id = ? AND pdt_id = ? AND opt_id = ? AND clist_select = 'Y'";
		  	    String updateSql = "UPDATE o_cartlist SET clist_pdt_count = clist_pdt_count + 1 WHERE user_id = ? AND pdt_id = ? AND opt_id = ? AND clist_select = 'Y'";
		  	    String insertSql = "INSERT INTO o_cartlist (clist_id, user_id, pdt_id, opt_id, clist_pdt_count, clist_adddate, clist_select) VALUES (addcart_seq.NEXTVAL, ?, ?, ?, 1, SYSDATE, 'Y')";
		  	    String productCountSql = "SELECT COUNT(DISTINCT pdt_id) AS product_count FROM o_cartlist WHERE user_id = ? AND clist_select = 'Y'";
		  	    
		  	    int productCount = 0;
		  	    int optionId = 0;
		  	    String message = "";
		  	    
		  	    try (Connection conn = ConnectionProvider.getConnection();
		  	         PreparedStatement optIdStmt = conn.prepareStatement(optIdSql);
		  	         PreparedStatement checkStmt = conn.prepareStatement(checkSql);
		  	         PreparedStatement updateStmt = conn.prepareStatement(updateSql);
		  	         PreparedStatement insertStmt = conn.prepareStatement(insertSql);
		  	         PreparedStatement productCountStmt = conn.prepareStatement(productCountSql)) {
		  	        
		  	        // 1. 옵션 ID 가져오기
		  	        optIdStmt.setInt(1, pdtId);
		  	        ResultSet optRs = optIdStmt.executeQuery();
		  	        if (optRs.next()) {
		  	            optionId = optRs.getInt("opt_id");
		  	        } else {
		  	            System.out.println("해당 제품에 옵션이 없습니다. 기본 옵션 ID 0으로 설정합니다.");
		  	            optionId = 0;
		  	        }

		  	        // 2. 장바구니에 상품 있는지 확인
		  	        checkStmt.setInt(1, userId);
		  	        checkStmt.setInt(2, pdtId);
		  	        checkStmt.setInt(3, optionId);
		  	        ResultSet rs = checkStmt.executeQuery();

		  	        if (rs.next()) {
		  	            // 3. 상품과 옵션이 존재하면 수량 증가
		  	            updateStmt.setInt(1, userId);
		  	            updateStmt.setInt(2, pdtId);
		  	            updateStmt.setInt(3, optionId);
		  	            updateStmt.executeUpdate();
		  	            message = "상품이 장바구니에 있어 상품 수량이 증가했습니다.";
		  	        } else {
		  	            // 4. 상품과 옵션이 존재하지 않으면 추가
		  	            insertStmt.setInt(1, userId);
		  	            insertStmt.setInt(2, pdtId);
		  	            insertStmt.setInt(3, optionId);
		  	            insertStmt.executeUpdate();
		  	            message = "장바구니에 추가되었습니다.";
		  	            
		  	            Integer oldProductCount = (Integer) request.getSession().getAttribute("productCount");
		  	            oldProductCount = oldProductCount + 1;
		  	            request.getSession().setAttribute("productCount", oldProductCount);
		  	        }

		  	        // 5. 유저의 총 상품 수 가져오기
		  	        productCountStmt.setInt(1, userId);
		  	        ResultSet countRs = productCountStmt.executeQuery();
		  	        if (countRs.next()) {
		  	            productCount = countRs.getInt("product_count");
		  	        }

		  	    } catch (SQLException e) {
		  	        e.printStackTrace();
		  	        message = "오류 발생: 장바구니 추가 실패";
		  	    } catch (NamingException e) {
		  	        e.printStackTrace();
		  	        message = "오류 발생: 장바구니 추가 실패";
		  	    }

		  	    // 결과를 Map에 담아 반환
		  	    Map<String, Object> result = new HashMap<>();
		  	    result.put("productCount", productCount);
		  	    result.put("message", message);
		  	    
		  	    return result;
		  	}
		  	// 회원 장바구니 버튼 클릭 시 디비 저장 끝
		  	
		  		
		  		
		  		// 회원 장바구니 select 처리 시작
		  		@Override
		  		public ArrayList<CartDTO> selectCartList(Integer userId) {
		  			
		  			String pdt_name; // 상품 이름
		  		    int pdt_amount; // 상품 가격
		  		    int clist_pdt_count; // 상품 수량
		  		    String clist_select; // 상품 체크 여부
		  		    int pdt_id; // 상품 id
		  		    String pdt_img_url; // 이미지 경로
		  		    int pdt_discount_rate; // 할인율
		  		    int opt_id; // 옵션 id
		  		    int clist_id; // 장바구니 id
		  		    
		  		    System.out.println("dao userId : " + userId);
		  		    
		  		    String updateSql = "UPDATE o_cartlist SET clist_select = 'Y' WHERE user_id = ?";
		  		    String selectSql = "SELECT b.pdt_name, b.pdt_amount, a.clist_pdt_count, a.clist_select, a.pdt_id, "
		  		                     + "b.pdt_img_url, b.pdt_discount_rate, a.opt_id, a.clist_id "
		  		                     + "FROM o_cartlist a "
		  		                     + "INNER JOIN o_product b ON a.pdt_id = b.pdt_id "
		  		                     + "WHERE a.user_id = ? "
		  		                     + "ORDER BY a.clist_adddate DESC";
		  		    
		  		    ArrayList<CartDTO> list = null;
		  		    CartDTO cartDTO = null;
		  		    
		  		    try {
		  		        // clist_select 값을 모두 'Y'로 초기화
		  		        pstmt = conn.prepareStatement(updateSql);
		  		        pstmt.setInt(1, userId);
		  		        pstmt.executeUpdate();
		  		        pstmt.close(); // 초기화 쿼리 후 pstmt 닫기
		  		        
		  		        // 장바구니 목록 조회
		  		        pstmt = conn.prepareStatement(selectSql);
		  		        pstmt.setInt(1, userId);
		  		        rs = pstmt.executeQuery();
		  		        
		  		        if (rs.next()) {
		  		            list = new ArrayList<CartDTO>();
		  		            do {
		  		                pdt_name = rs.getString("pdt_name");
		  		                pdt_amount = rs.getInt("pdt_amount");
		  		                clist_pdt_count = rs.getInt("clist_pdt_count");
		  		                clist_select = rs.getString("clist_select");
		  		                pdt_id = rs.getInt("pdt_id");
		  		                pdt_img_url = rs.getString("pdt_img_url");
		  		                pdt_discount_rate = rs.getInt("pdt_discount_rate");
		  		                opt_id = rs.getInt("opt_id");
		  		                clist_id = rs.getInt("clist_id");
		  		                
		  		                cartDTO = new CartDTO().builder()
		  		                        .pdt_name(pdt_name)
		  		                        .pdt_amount(pdt_amount)
		  		                        .clist_pdt_count(clist_pdt_count)
		  		                        .clist_select(clist_select)
		  		                        .pdt_id(pdt_id)
		  		                        .pdt_img_url(pdt_img_url)
		  		                        .pdt_discount_rate(pdt_discount_rate)
		  		                        .opt_id(opt_id)
		  		                        .clist_id(clist_id)
		  		                        .build();
		  		                
		  		                list.add(cartDTO);
		  		                
		  		            } while (rs.next());
		  		        }
		  		        
		  		    } catch (Exception e) {
		  		        e.printStackTrace();
		  		    } finally {
		  		        try {
		  		            if (rs != null) rs.close();
		  		            if (pstmt != null) pstmt.close();
		  		        } catch (Exception e) {
		  		            e.printStackTrace();
		  		        }
		  		    }
		  		    
		  		    return list;
		  		    
		  		}
		  		// 회원 장바구니 select 처리 끝
		  		
		  		
		  		// 회원 장바구니 삭제 시작
		  		@Override
		  		public boolean delCart(int clistId, HttpServletRequest request) {
		  			
		  			System.out.println("dao clistId : " + clistId);
		  			
		  		    String sql = "DELETE FROM o_cartlist WHERE clist_id = ?";
		  		    boolean isDeleted = false; // 삭제 성공 여부를 저장할 변수
		  		    
		  		    try (Connection conn = ConnectionProvider.getConnection();
		  		         PreparedStatement productCountStmt = conn.prepareStatement(sql)) {
		  		        
		  		        productCountStmt.setInt(1, clistId);
		  		        
		  		        int affectedRows = productCountStmt.executeUpdate(); // 쿼리 실행
		  		        
		  		        if (affectedRows > 0) {
		  		            isDeleted = true; // 삭제 성공
		  		            
		  		            Integer oldProductCount = (Integer) request.getSession().getAttribute("productCount");
		  		            oldProductCount = oldProductCount - 1;
		  		            request.getSession().setAttribute("productCount", oldProductCount);
		  		        } // if
		  		        
		  		    } catch (Exception e) {
		  		        e.printStackTrace();
		  		    } // try catch
		  		    
		  		    return isDeleted; // 삭제 성공 여부 반환
		  		}
		  		// 회원 장바구니 삭제 끝
		  		
		  		// 회원 장바구니 상품 수량 증가 시작
		  		@Override
		  		public boolean pdtCountUp(int clistId, int pdtId, int optId) {
		  			
		  			System.out.println("dao clistId : " + clistId);
		  		    System.out.println("dao pdtId : " + pdtId);
		  		    System.out.println("dao optId : " + optId);
		  		    
		  		    String pdtCountSql = "SELECT pdt_count FROM o_product WHERE pdt_id = ?";
		  		    String optCountSql = "SELECT opt_count FROM o_pdtoption WHERE opt_id = ? AND pdt_id = ?";
		  		    String cartCountSql = "SELECT clist_pdt_count FROM o_cartlist WHERE clist_id = ?";
		  		    String upPdtCountSql = "UPDATE o_cartlist SET clist_pdt_count = clist_pdt_count + 1 WHERE clist_id = ?";
		  		    boolean isUpdate = false;
		  		    
		  		    try (Connection conn = ConnectionProvider.getConnection()) {
		  		        int availableStock = 0;
		  		        int currentCartCount = 0;

		  		        // 장바구니의 현재 상품 수량 확인
		  		        try (PreparedStatement cartPstmt = conn.prepareStatement(cartCountSql)) {
		  		            cartPstmt.setInt(1, clistId);
		  		            try (ResultSet rs = cartPstmt.executeQuery()) {
		  		                if (rs.next()) {
		  		                    currentCartCount = rs.getInt("clist_pdt_count");
		  		                }
		  		            }
		  		        }

		  		        // 옵션이 있는 상품일 때 재고 확인
		  		        if (optId != 0) {
		  		            try (PreparedStatement pstmt = conn.prepareStatement(optCountSql)) {
		  		                pstmt.setInt(1, optId);
		  		                pstmt.setInt(2, pdtId);
		  		                try (ResultSet rs = pstmt.executeQuery()) {
		  		                    if (rs.next()) {
		  		                        availableStock = rs.getInt("opt_count");
		  		                    }
		  		                }
		  		            }
		  		        } else { // 옵션이 없는 일반 상품일 때 재고 확인
		  		            try (PreparedStatement pstmt = conn.prepareStatement(pdtCountSql)) {
		  		                pstmt.setInt(1, pdtId);
		  		                try (ResultSet rs = pstmt.executeQuery()) {
		  		                    if (rs.next()) {
		  		                        availableStock = rs.getInt("pdt_count");
		  		                    }
		  		                }
		  		            }
		  		        }

		  		        System.out.println("현재 장바구니 수량: " + currentCartCount);
		  		        System.out.println("상품 재고: " + availableStock);

		  		        // 재고 확인: 재고보다 장바구니 수량이 많아질 경우 오류 출력
		  		        if (currentCartCount + 1 > availableStock) {
		  		            System.out.println("오류: 재고보다 많은 수량을 요청했습니다.");
		  		            return false;
		  		        }

		  		        // 재고보다 수량이 적거나 같을 경우 수량 증가
		  		        try (PreparedStatement pstmt = conn.prepareStatement(upPdtCountSql)) {
		  		            pstmt.setInt(1, clistId);
		  		            int success = pstmt.executeUpdate();
		  		            if (success > 0) {
		  		                isUpdate = true;
		  		            }
		  		        }

		  		    } catch (Exception e) {
		  		        e.printStackTrace();
		  		    }
		  		    
		  		    return isUpdate;
		  		    
		  		}
		  		// 회원 장바구니 상품 수량 증가 끝
		  		
		  		// 회원 장바구니 상품 수량 감소 시작
		  		@Override
		  		public boolean pdtCountDown(int clistId) {
		  			
		  			System.out.println("dao clistId : " + clistId);
		  		    
		  		    String clistPdtCountSql = "SELECT clist_pdt_count FROM o_cartlist WHERE clist_id = ?";
		  		    String clistPdtCountDownSql = "UPDATE o_cartlist SET clist_pdt_count = clist_pdt_count - 1 WHERE clist_id = ?";
		  		    boolean isUpdate = false;

		  		    try (Connection conn = ConnectionProvider.getConnection()) {
		  		        // 기존 상품 수량 조회
		  		        try (PreparedStatement pstmt = conn.prepareStatement(clistPdtCountSql)) {
		  		            pstmt.setInt(1, clistId);
		  		            try (ResultSet rs = pstmt.executeQuery()) {
		  		                if (rs.next()) {
		  		                    int clist_pdt_count = rs.getInt("clist_pdt_count");

		  		                    if (clist_pdt_count > 1) {
		  		                        // 상품 수량 감소
		  		                        try (PreparedStatement updatePstmt = conn.prepareStatement(clistPdtCountDownSql)) {
		  		                            updatePstmt.setInt(1, clistId);
		  		                            int success = updatePstmt.executeUpdate();
		  		                            if (success > 0) {
		  		                                isUpdate = true;
		  		                            }
		  		                        }
		  		                    }
		  		                }
		  		            }
		  		        }
		  		    } catch (Exception e) {
		  		        e.printStackTrace();
		  		    }

		  		    return isUpdate;
		  		    
}
		  		
}
