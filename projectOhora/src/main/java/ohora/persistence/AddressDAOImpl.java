package ohora.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ohora.domain.AddressDTO;
import ohora.domain.MyPageDTO;

public class AddressDAOImpl implements AddressDAO {
	
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	public AddressDAOImpl(Connection conn) {
		super();
		this.conn = conn;
	}
	
	// address 이름 처리 부분 시작 / myPageDTO 쓴 이유 : 똑같아서
	@Override
	public ArrayList<MyPageDTO> addressName(Integer userId) {
		
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
	// address 이름 처리 부분 끝
	
	// address 주소 부분 select 시작
	@Override
	public ArrayList<AddressDTO> addressSel(Integer userId) {
		
		System.out.println("dao userId : " + userId);
		
		int addr_id; // 주소 id
		String addr_nick; // 예) 학원, 집
		String addr_name; // 이름
		String addr_htel; // 집 번호
		String addr_tel; // 휴대폰 번호
	    String addr_address_main;  // 주소
	    String addr_address_detail; // 상세 주소
	    String addr_zipcode; // 우편번호
	    String addr_main; // 메인으로 쓰일 주소 Y / N
		String sql;

		sql = " SELECT * ";
		sql += " FROM o_address ";
		sql += " WHERE user_id = ? ";
		sql += " ORDER BY ";
		sql += " CASE ";
		sql += " WHEN addr_main = 'Y' THEN 1 ";
		sql += " ELSE 2 ";
		sql += " END, ";
		sql += " addr_id DESC ";

		ArrayList<AddressDTO> addressList = null;
		AddressDTO addressDTO = null;

		try {

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				addressList = new ArrayList<AddressDTO>();
				do {
					addr_id = rs.getInt("addr_id");
					addr_nick = rs.getString("addr_nick");
					addr_name = rs.getString("addr_name");
					addr_htel = rs.getString("addr_htel");
					addr_tel = rs.getString("addr_tel");
					addr_address_main = rs.getString("addr_address_main");
					addr_address_detail = rs.getString("addr_address_detail");
					addr_zipcode = rs.getString("addr_zipcode");
					addr_main = rs.getString("addr_main");

					addressDTO = new AddressDTO().builder()
							.addr_id(addr_id)
							.addr_nick(addr_nick)
							.addr_name(addr_name)
							.addr_htel(addr_htel)
							.addr_tel(addr_tel)
							.addr_address_main(addr_address_main)
							.addr_address_detail(addr_address_detail)
							.addr_zipcode(addr_zipcode)
							.addr_main(addr_main)
							.build();

					addressList.add(addressDTO);

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

		return addressList;
		
	}
	// address 주소 부분 select 끝
	
	// address 주소 메인 처리 시작
	@Override
	public boolean addressMain(int addrId, int userId) {
		
		String allSql = "UPDATE o_address SET addr_main = 'N' WHERE user_id = ?";
	    String soloSql = "UPDATE o_address SET addr_main = 'Y' WHERE addr_id = ?";
	    boolean success = false;

	    try {
	        // 모든 주소를 메인 해제
	        pstmt = conn.prepareStatement(allSql);
	        pstmt.setInt(1, userId);
	        pstmt.executeUpdate();

	        // 특정 주소를 메인으로 설정
	        pstmt = conn.prepareStatement(soloSql);
	        pstmt.setInt(1, addrId);
	        int rowsAffected = pstmt.executeUpdate();
	        if (rowsAffected > 0) {
	            success = true;
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (pstmt != null) pstmt.close();
	            if (conn != null) conn.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    return success;
	    
	}
	// address 주소 메인 처리 끝
	
	// address 삭제 버튼 처리 시작
	   @Override
	   public boolean addressDelBtn(String[] addrIdsStr) {
	      
	      boolean isCheck = true;
	      
	      if (addrIdsStr != null) {
	           int[] addrIds = new int[addrIdsStr.length];  // int 배열을 addrIdsStr 배열의 크기로 생성
	           
	           // 각 문자열을 int로 변환하여 addrIds 배열에 저장
	           for (int i = 0; i < addrIdsStr.length; i++) {
	               try {
	                   addrIds[i] = Integer.parseInt(addrIdsStr[i]);  // String을 int로 변환
	               } catch (NumberFormatException e) {
	                   System.out.println("유효하지 않은 addr_id 값: " + addrIdsStr[i]);
	                   // 예외 처리: 유효하지 않은 숫자일 경우, 기본값을 설정하거나 예외 처리
	                   return false;  // 잘못된 값이 있을 경우 false 반환
	               }
	           }

	           // 변환된 addrIds 배열 출력 (디버깅용)
	           for (int addrId : addrIds) {
	               System.out.println("삭제할 주소 ID: " + addrId);
	           }
	           
	           try {
	               String sql = "DELETE FROM o_address WHERE addr_id = ?";  // 주소 삭제 쿼리

	               pstmt = conn.prepareStatement(sql);
	               for (int addrId : addrIds) {
	                   pstmt.setInt(1, addrId);
	                   pstmt.executeUpdate();  // 각 addr_id에 대해 삭제 쿼리 실행
	               }

	               return isCheck;  // 삭제 성공
	           } catch (SQLException e) {
	               System.out.println("주소 삭제 실패: " + e.getMessage());
	               return false;  // 삭제 실패
	           } finally {
	               try {
	                   if (pstmt != null) pstmt.close();
	                   if (conn != null) conn.close();
	               } catch (SQLException e) {
	                   System.out.println("리소스 해제 실패: " + e.getMessage());
	               }
	           }
	       }
	      
	       return false;
	       
	   }
	   // address 삭제 버튼 처리 끝
	
} // class
