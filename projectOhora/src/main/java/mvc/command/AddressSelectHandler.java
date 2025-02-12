package mvc.command;

import java.nio.channels.UnresolvedAddressException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.util.ConnectionProvider;

import ohora.domain.AddressDTO;
import ohora.domain.MyPageDTO;
import ohora.persistence.AddressDAO;
import ohora.persistence.AddressDAOImpl;

public class AddressSelectHandler implements CommandHandler {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		System.out.println("addressSelectHandler test");
		
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");
		
		Connection conn = ConnectionProvider.getConnection();
		AddressDAO dao = new AddressDAOImpl(conn);
		
		ArrayList<MyPageDTO> list = null;
		ArrayList<AddressDTO> addressList = null;
		
		try {
			
			// 이름 부분
			list = dao.addressName(userId);
			request.setAttribute("addressNameList", list);
			
			// 배송 주소 부분
			addressList = dao.addressSel(userId);
			request.setAttribute("addressSelList", addressList);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		} // try catch finally
		
		// 포워딩
		RequestDispatcher dispatcher = request.getRequestDispatcher("/ohora/addr.jsp");
		dispatcher.forward(request, response);
		
		return null;
		
	}

}
