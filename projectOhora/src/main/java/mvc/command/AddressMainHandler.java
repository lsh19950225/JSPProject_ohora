package mvc.command;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.util.ConnectionProvider;

import ohora.persistence.AddressDAO;
import ohora.persistence.AddressDAOImpl;

public class AddressMainHandler implements CommandHandler {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		System.out.println("addressMainHandler test");
		
		String contextPath = request.getContextPath();
		
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");
		
		int addrId = Integer.parseInt( request.getParameter("addr_id") );
		System.out.println("addrId : " + addrId);
		
		boolean success;
		
		Connection conn = ConnectionProvider.getConnection();
		AddressDAO dao = new AddressDAOImpl(conn);
		
		try {
			
			success = dao.addressMain(addrId, userId);
			
			if (success) {
				// 리다이렉트
				response.sendRedirect( contextPath + "/address/addressselect.do" );
			} // if
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		} // try catch finally
		
		return null;
		
	}
	
} // class
