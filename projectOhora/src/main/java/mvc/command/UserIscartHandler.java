package mvc.command;

import java.sql.Connection; 
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.ConnectionProvider;

import ohora.domain.CartDTO;

import ohora.persistence.UserCartDAO;
import ohora.persistence.UserCartDAOImpl;

public class UserIscartHandler implements CommandHandler {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		System.out.println("userIscartHandler test");
		
		String contextPath = request.getContextPath();
		
		// 세션 객체 가져오기
        Integer userId = (Integer) request.getSession().getAttribute("userId");
		
		// 값 확인
		System.out.println("userId : " + userId);
		
		try (Connection conn = ConnectionProvider.getConnection()) {
			
			UserCartDAO dao = new UserCartDAOImpl(conn);
			
			ArrayList<CartDTO> list = dao.selectCartList(userId);
			
			request.setAttribute("cartList", list);
			
		} catch (Exception e) {
			e.printStackTrace();
		} // try catch
		
		// 포워딩
		RequestDispatcher dispatcher = request.getRequestDispatcher("/ohora/iscart.jsp");
        dispatcher.forward(request, response);
		
		return null;
	}

}
