package mvc.command;

import java.sql.Connection; 
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.util.ConnectionProvider;

import ohora.domain.MyPageDTO;
import ohora.persistence.MypageDAO;
import ohora.persistence.MypageDAOImpl;


public class MyPageHandler implements CommandHandler {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		System.out.println("myPageHandler test");
		
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");
		Integer productCount = (Integer) session.getAttribute("productCount");
		
		System.out.println("userId : " + userId);
		System.out.println("productCount : " + productCount);
		
		Connection conn = ConnectionProvider.getConnection();
		MypageDAO dao = new MypageDAOImpl(conn);
		
		ArrayList<MyPageDTO> list = null;
		ArrayList<MyPageDTO> orderList = null;
		ArrayList<MyPageDTO> orderDetail = null;
		
		try {
			
			// 이름 부분
			list = dao.myPageName(userId);
			request.setAttribute("myPageList", list);
			
			// order list 부분
			orderList = dao.orderList(userId);
			request.setAttribute("myPageOrderList", orderList);
			
			// 주문 select
			orderDetail = dao.ordDetail(userId);
			// System.out.println("orderDetail : " + orderDetail);
			request.setAttribute("myPageOrderDetail", orderDetail);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		} // try catch finally
		
		// 포워딩
		RequestDispatcher dispatcher = request.getRequestDispatcher("/ohora/oho_mypage.jsp");
		dispatcher.forward(request, response);
		
		return null;
		
	}

}
