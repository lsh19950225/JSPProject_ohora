package mvc.command;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.util.ConnectionProvider;

import ohora.domain.AddrDTO;
import ohora.domain.AddressDTO;
import ohora.domain.UserDTO;
import ohora.persistence.MemberDAO;
import ohora.persistence.MemberDAOImpl;

public class AddressModifyHandler implements CommandHandler{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");
		
		String locationName = request.getParameter("ma_rcv_title"); 
		String receiverName = request.getParameter("ma_rcv_name");	
		String zipCode = request.getParameter("address_zip1");
		String addr1 = request.getParameter("address_addr1"); //기본주소
		String addr2 = request.getParameter("address_addr2"); //상세주소
		String mobile0 =request.getParameter("ma_rcv_mobile_no");
		String mobile1 =request.getParameter("ma_rcv_mobile_no2");
		String mobile2 =request.getParameter("ma_rcv_mobile_no3");
		
		String mobile = mobile0 + "-" + mobile1 + "-" + mobile2;
		System.out.println("합성된 전화번호 > " + mobile );
		System.out.println("배송지명 > " + locationName);
		System.out.println(request.getParameter("ma_rcv_title"));
		Connection conn = ConnectionProvider.getConnection();
		MemberDAO dao = new MemberDAOImpl(conn);

		AddressDTO dto = new AddressDTO();
			dto.setAddr_id(0);
			dto.setUser_id(userId);
			dto.setAddr_nick(locationName);
			dto.setAddr_name(receiverName);
			dto.setAddr_zipcode(zipCode);
			dto.setAddr_address_main(addr1);
			dto.setAddr_address_detail(addr2);
			dto.setAddr_tel(mobile);
			
			
		try {
			int rowCount = dao.insertAddr(conn, dto);
			
			
			if ( rowCount == 1) System.out.println(" 배송지 등록 완료 ");
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		
		String path = "/projectOhora/address/addressselect.do";
		response.sendRedirect(path);
		
		return null;
	}

}
