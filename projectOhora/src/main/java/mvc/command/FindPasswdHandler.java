package mvc.command;

import java.sql.Connection; 

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import com.util.ConnectionProvider;

import ohora.domain.UserDTO;
import ohora.persistence.MemberDAO;
import ohora.persistence.MemberDAOImpl;


public class FindPasswdHandler implements CommandHandler {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		System.out.println("findPasswdHandler test");
		
		String contextPath = request.getContextPath();
		String userID = request.getParameter("user_id");
		String name = request.getParameter("name");
		
		String contactMethod = request.getParameter("contact_method");
		
		System.out.println("userID : " + userID + "\n");
		System.out.println("name : " + name + "\n");
		System.out.println("contactMethod : " + contactMethod + "\n");
		
		UserDTO userFindPasswd = null;
		
		if ( "email".equals(contactMethod) ) {
			String email = request.getParameter("email");
			System.out.println("email : " + email + "\n");
			request.setAttribute("emailOrPhone", email);
			System.out.println("set " + email);
			
			userFindPasswd = UserDTO.builder()
					.user_login_id(userID)
					.user_name(name)
					.user_email(email)
					.build();
		} else {
			String phone = request.getParameter("phone");
			System.out.println("phone : " + phone + "\n");
			request.setAttribute("emailOrPhone", phone);
			
			userFindPasswd = UserDTO.builder()
					.user_login_id(userID)
					.user_name(name)
					.user_tel(phone)
					.build();
		} // if else
		
		boolean check;
		
		Connection conn = ConnectionProvider.getConnection();
		MemberDAO dao = new MemberDAOImpl(conn);
		
		try {
			
			check = dao.findPasswd(userFindPasswd);
			
			if ( check ) {
				request.setAttribute("contactMethod", contactMethod);
				System.out.println("set " + contactMethod);
				HttpSession session = request.getSession();
				session.setAttribute("userID", userID);
				System.out.println("set " + userID);
				// 포워딩
				RequestDispatcher dispatcher = request.getRequestDispatcher( "/ohora/checkPasswd.jsp" );
				dispatcher.forward(request, response);
				return null;
			} else {
				response.sendRedirect( contextPath + "/ohora/findPasswd.jsp?fail=true" );
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
