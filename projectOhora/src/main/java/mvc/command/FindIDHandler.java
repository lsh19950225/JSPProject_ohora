package mvc.command;

import java.sql.Connection; 

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.util.ConnectionProvider;

import ohora.domain.UserDTO;
import ohora.persistence.MemberDAO;
import ohora.persistence.MemberDAOImpl;

import ohora.persistence.UserCartDAO;
import ohora.persistence.UserCartDAOImpl;

public class FindIDHandler implements CommandHandler {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		// post 로 받아옴
		
		String contextPath = request.getContextPath();
		
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String phone = request.getParameter("phone");
		
		System.out.println("name : " + name + "\n");
		System.out.println("email : " + email + "\n");
		System.out.println("phone : " + phone + "\n");
		
		Connection conn = ConnectionProvider.getConnection();
		MemberDAO dao = new MemberDAOImpl(conn);
		
		UserDTO user = UserDTO.builder()
				.user_name(name)
				.user_tel(phone != null ? phone : null)
				.user_email(email != null ? email : null)
				.build();
		
		try {
			String findUserID = dao.findUser(user);
			
			System.out.println("find ID : " + findUserID);
			
			if ( findUserID != null ) {
				request.setAttribute("findUserID", findUserID);
				// 포워딩
				RequestDispatcher dispatcher = request.getRequestDispatcher( "/ohora/showUserID.jsp" );
		        dispatcher.forward(request, response);
				return null;
			} else {
				// 리다이렉트
				response.sendRedirect( contextPath + "/ohora/findID.jsp?fail=true" );
				return null;
			} // if else
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) conn.close();
		} // try catch finally
		
		return null;
		
	}
	
}
