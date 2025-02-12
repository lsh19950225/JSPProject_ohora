package mvc.command;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.util.ConnectionProvider;

import ohora.domain.CartDTO;
import ohora.domain.orderDetailDTO;
import ohora.persistence.MemberDAO;
import ohora.persistence.MemberDAOImpl;
import ohora.persistence.MypageDAO;
import ohora.persistence.MypageDAOImpl;
import ohora.persistence.UserCartDAO;
import ohora.persistence.UserCartDAOImpl;

public class OrderDetailHandler implements CommandHandler {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		System.out.println("orderDetailHandler test");
		
		int ord_pk = Integer.parseInt( request.getParameter("ord_pk") );
		System.out.println("ord_pk : " + ord_pk);
		
		int opdt_id = Integer.parseInt( request.getParameter("opdt_id") );
		System.out.println("opdt_id : " + opdt_id);
		
		HttpSession session = request.getSession();
		Integer userId = (Integer) session.getAttribute("userId");
		
		String opdt_name = request.getParameter("opdt_name");
		System.out.println("opdt_name : " + opdt_name);
		
        try (Connection conn = ConnectionProvider.getConnection()) {
			
        	MypageDAO dao = new MypageDAOImpl(conn);
			
        	// 주문정보, 결제정보 부분
			ArrayList<orderDetailDTO> topList = dao.selectOrderDetail(ord_pk, userId);
			request.setAttribute("topList", topList);
			
			// 주문 상품 정보
			ArrayList<orderDetailDTO> orderList = dao.selectOrderList(ord_pk, userId, opdt_name, opdt_id);
			request.setAttribute("orderList", orderList);
			
			// 배송지정보 부분
			ArrayList<orderDetailDTO> addrList = dao.selectAddrList(userId);
			request.setAttribute("addrList", addrList);
			
		} catch (Exception e) {
			e.printStackTrace();
		} // try catch
		
		// 포워딩
		RequestDispatcher dispatcher = request.getRequestDispatcher("/ohora/order_detail.jsp");
        dispatcher.forward(request, response);
		
		return null;
		
	}

}
