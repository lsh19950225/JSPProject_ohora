package mvc.command;

import java.sql.Connection; 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.ConnectionProvider;


import ohora.persistence.UserCartDAO;
import ohora.persistence.UserCartDAOImpl;

public class DelBtnHandler implements CommandHandler {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		System.out.println("delBtnHandler test");
		
		String contextPath = request.getContextPath();
		int clistId = Integer.parseInt( request.getParameter("clist_id") );
		System.out.println("clistId : " + clistId);
		
		Connection conn = ConnectionProvider.getConnection();
		UserCartDAO dao = new UserCartDAOImpl(conn);
		
		try {
			boolean isDeleted = dao.delCart(clistId, request);
			
			System.out.println(isDeleted);
			
			if ( isDeleted ) {
				// 리다이렉트
				response.sendRedirect( contextPath + "/cart/useriscart.do" );
			} // if
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				} // try catch
			} // if
		} // try catch finally
				
		return null;
	}

}
