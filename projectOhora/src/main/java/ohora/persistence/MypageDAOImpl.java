package ohora.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import ohora.domain.MyPageDTO;
import ohora.domain.orderDetailDTO;

public class MypageDAOImpl implements MypageDAO{

	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private Statement stmt = null;
	private ResultSet rs = null;

	public MypageDAOImpl(Connection conn) {
		super();
		this.conn = conn;
	}
	
	@Override
	public ArrayList<MyPageDTO> myPageName(Integer userId) {
		
		System.out.println("dao userId : " + userId);

		String user_name;
		String mem_name;
		int user_point;
		String sql;

		sql = " select a.user_name, b.mem_name, a.user_point ";
		sql += " from o_user a ";
		sql += " inner join o_membership b on a.mem_id = b.mem_id ";
		sql += " where user_id = ? ";

		ArrayList<MyPageDTO> list = null;
		MyPageDTO myPageDTO = null;

		try {

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				list = new ArrayList<MyPageDTO>();
				do {
					user_name = rs.getString("user_name");
					mem_name = rs.getString("mem_name");
					user_point = rs.getInt("user_point");

					myPageDTO = new MyPageDTO().builder()
							.user_name(user_name)
							.mem_name(mem_name)
							.user_point(user_point)
							.build();

					list.add(myPageDTO);

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
	// 마이페이지 이름 처리 끝

	// 마이페이지 order list 시작
	@Override
	public ArrayList<MyPageDTO> orderList(Integer userId) {

		System.out.println("dao userId : " + userId);
		String sql;
		String opdt_state;

		sql = "SELECT b.opdt_state " +
				"FROM o_order a " +
				"INNER JOIN o_orddetail b ON a.ord_pk = b.ord_pk " +
				"WHERE a.user_id = ?";

		ArrayList<MyPageDTO> orderList = new ArrayList<>();
		MyPageDTO myPageDTO;

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				opdt_state = rs.getString("opdt_state");

				myPageDTO = new MyPageDTO().builder()
						.opdt_state(opdt_state)
						.build();

				orderList.add(myPageDTO);
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

		return orderList;

	}
	// 마이페이지 order list 끝

	// 마이페이지 주문 부분 select 시작
	@Override
	public ArrayList<MyPageDTO> ordDetail(Integer userId) {

		System.out.println("dao userId : " + userId);

		Date ord_orderdate;
		String opdt_name;
		int opdt_amount;
		// 추가
		int opdt_id;
		int ord_pk;
		String sql;
		
		// 추가
		sql = " select a.ord_orderdate, b.opdt_name, b.opdt_amount, b.ord_pk, b.opdt_id ";
		sql += " from o_order a ";
		sql += " inner join o_orddetail b on a.ord_pk = b.ord_pk ";
		sql += " where user_id = ? ";
		sql += " order by a.ord_orderdate desc ";

		ArrayList<MyPageDTO> orderDetail = null;
		MyPageDTO myPageDTO = null;

		try {

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				orderDetail = new ArrayList<MyPageDTO>();
				do {
					// 추가
					ord_pk = rs.getInt("ord_pk");
					ord_orderdate = rs.getDate("ord_orderdate");
					opdt_name = rs.getString("opdt_name");
					opdt_amount = rs.getInt("opdt_amount");
					opdt_id = rs.getInt("opdt_id");

					myPageDTO = new MyPageDTO().builder()
							.ord_pk(ord_pk)
							.ord_orderdate(ord_orderdate)
							.opdt_name(opdt_name)
							.opdt_amount(opdt_amount)
							.opdt_id(opdt_id)
							.build();

					orderDetail.add(myPageDTO);

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

		return orderDetail;

	}
	// 마이페이지 주문 부분 select 끝
	
	// orderDetail 주문정보, 결제정보 select 시작
		@Override
		public ArrayList<orderDetailDTO> selectOrderDetail(int ord_pk, Integer userId) {
			
			String ord_id;
			Date ord_orderdate;
			String ord_name;
			String opdt_state;
			int ord_total_amount;
			int discount;
			int amount;
			String ord_pay_option;
			
			String sql;
			
			sql = " select a.ord_id, a.ord_orderdate, a.ord_name, b.opdt_state, a.ord_total_amount, (a.ord_cpn_discount + a.ord_pdt_discount) discount, a.ord_total_amount - (a.ord_cpn_discount + a.ord_pdt_discount) amount, a.ord_pay_option ";
			sql += " from o_order a ";
			sql += " inner join o_orddetail b on a.ord_pk = b.ord_pk ";
			sql += " where user_id = ? and a.ord_pk = ? ";
			
			ArrayList<orderDetailDTO> topList = null;
			orderDetailDTO orderDetailDTO = null;
			
			try {
			        // 조회
			        pstmt = conn.prepareStatement(sql);
			        pstmt.setInt(1, userId);
			        pstmt.setInt(2, ord_pk);
			        
			        rs = pstmt.executeQuery();
			        
			        if (rs.next()) {
			        	topList = new ArrayList<orderDetailDTO>();
			            do {
			            	ord_id = rs.getString("ord_id");
			            	ord_orderdate = rs.getDate("ord_orderdate");
			            	ord_name = rs.getString("ord_name");
			            	opdt_state = rs.getString("opdt_state");
			            	ord_total_amount = rs.getInt("ord_total_amount");
			            	discount = rs.getInt("discount");
			            	amount = rs.getInt("amount");
			            	ord_pay_option = rs.getString("ord_pay_option");
			                
			            	orderDetailDTO = new orderDetailDTO().builder()
			                        .ord_id(ord_id)
			                        .ord_orderdate(ord_orderdate)
			                        .ord_name(ord_name)
			                        .opdt_state(opdt_state)
			                        .ord_total_amount(ord_total_amount)
			                        .discount(discount)
			                        .amount(amount)
			                        .ord_pay_option(ord_pay_option)
			                        .build();
			                
			            	topList.add(orderDetailDTO);
			                
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
			    
			    return topList;
			
		}
		// orderDetail 주문정보, 결제정보 select 끝
		
		// orderDetail 주문 상품 정보 select 시작
		@Override
		public ArrayList<orderDetailDTO> selectOrderList(int ord_pk, Integer userId, String opdt_name, int opdt_id) {
			
			String orderSql = "SELECT b.opdt_name, b.opdt_count, b.opdt_state FROM o_order a INNER JOIN o_orddetail b ON a.ord_pk = b.ord_pk WHERE user_id = ? AND opdt_id = ?";
		    String productSql = "SELECT pdt_img_url, pdt_name, pdt_amount, pdt_amount * (1 - COALESCE(pdt_discount_rate, 0) / 100) AS discount_price FROM o_product WHERE pdt_name = ?";

		    ArrayList<orderDetailDTO> orderList = new ArrayList<>();
		    
		    try (PreparedStatement orderStmt = conn.prepareStatement(orderSql);
		         PreparedStatement productStmt = conn.prepareStatement(productSql)) {

		        // First query execution
		        orderStmt.setInt(1, userId);
		        orderStmt.setInt(2, opdt_id);
		        try (ResultSet orderRs = orderStmt.executeQuery()) {
		            while (orderRs.next()) {
		                String opdtName = orderRs.getString("opdt_name");
		                int opdtCount = orderRs.getInt("opdt_count");
		                String opdtState = orderRs.getString("opdt_state");

		                // Create DTO for order detail
		                orderDetailDTO orderDetail = new orderDetailDTO().builder()
		                        .opdt_name(opdtName)
		                        .opdt_count(opdtCount)
		                        .opdt_state(opdtState)
		                        .build();

		                // Second query execution using product name
		                productStmt.setString(1, opdtName);
		                try (ResultSet productRs = productStmt.executeQuery()) {
		                    if (productRs.next()) {
		                        String pdtImgUrl = productRs.getString("pdt_img_url");
		                        String pdtName = productRs.getString("pdt_name");
		                        int pdtAmount = productRs.getInt("pdt_amount");
		                        int discountPrice = productRs.getInt("discount_price");

		                        // Add product details to DTO
		                        orderDetail.setPdt_img_url(pdtImgUrl);
		                        orderDetail.setPdt_name(pdtName);
		                        orderDetail.setPdt_amount(pdtAmount);
		                        orderDetail.setDiscount_price(discountPrice);
		                    }
		                }

		                // Add orderDetailDTO to the list
		                orderList.add(orderDetail);
		            }
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		    return orderList;

		}
		// orderDetail 주문 상품 정보 select 끝
		
		// orderDetail 배송지정보 select 시작
		@Override
		public ArrayList<orderDetailDTO> selectAddrList(Integer userId) {
			
			String sql;
			
			String addr_name;
		    String addr_zipcode;
		    String addr_address_main;
		    String addr_htel;
		    String addr_tel;
			
			sql = " select addr_name, addr_zipcode, addr_address_main, addr_htel, addr_tel ";
			sql += " from o_address ";
			sql += " where user_id = ? and addr_main = 'Y' ";
			
			ArrayList<orderDetailDTO> addrList = null;
			orderDetailDTO orderDetailDTO = null;
			
			try {
			        // 조회
			        pstmt = conn.prepareStatement(sql);
			        pstmt.setInt(1, userId);
			        
			        rs = pstmt.executeQuery();
			        
			        if (rs.next()) {
			        	addrList = new ArrayList<orderDetailDTO>();
			            do {
			            	addr_name = rs.getString("addr_name");
			            	addr_zipcode = rs.getString("addr_zipcode");
			            	addr_address_main = rs.getString("addr_address_main");
			            	addr_htel = rs.getString("addr_htel");
			            	addr_tel = rs.getString("addr_tel");
			                
			            	orderDetailDTO = new orderDetailDTO().builder()
			                        .addr_name(addr_name)
			                        .addr_zipcode(addr_zipcode)
			                        .addr_address_main(addr_address_main)
			                        .addr_htel(addr_htel)
			                        .addr_tel(addr_tel)
			                        .build();
			                
			            	addrList.add(orderDetailDTO);
			                
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
			    
			    return addrList;
			    
		}
		// orderDetail 배송지정보 select 시작
		
}
