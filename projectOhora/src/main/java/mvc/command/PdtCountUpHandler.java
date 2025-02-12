package mvc.command;
 
import java.sql.Connection; 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.ConnectionProvider;

import ohora.persistence.UserCartDAO;
import ohora.persistence.UserCartDAOImpl;

public class PdtCountUpHandler implements CommandHandler {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		System.out.println("pdtCountUpHandler test");

        // 클라이언트에서 전달된 파라미터를 받는다
        int clistId = Integer.parseInt(request.getParameter("clist_id"));
        int pdtId = Integer.parseInt(request.getParameter("pdt_id"));
        int optId = Integer.parseInt(request.getParameter("opt_id"));
        
        String contextPath = request.getContextPath();

        System.out.println("clistId : " + clistId);
        System.out.println("pdtId : " + pdtId);
        System.out.println("optId : " + optId);
        
        try (Connection conn = ConnectionProvider.getConnection()) {
        	UserCartDAO dao = new UserCartDAOImpl(conn);
            
            // 상품 수량 증가 처리
            boolean isUpdated = dao.pdtCountUp(clistId, pdtId, optId);
            
            if (isUpdated) {
                // 수량 증가가 성공적으로 처리되었을 때
                System.out.println("장바구니 상품 수량 증가 성공");
                response.sendRedirect( contextPath + "/cart/useriscart.do" );
                return null;
            } else {
                // 재고 부족 또는 다른 이유로 실패한 경우
                System.out.println("수량 증가 실패: 재고 부족 또는 오류");
                request.setAttribute("error", "재고보다 많은 수량을 요청했습니다.");
                response.sendRedirect( contextPath + "/cart/useriscart.do" );
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "상품 수량 증가 처리 중 오류가 발생했습니다.");
            response.sendRedirect( contextPath + "/cart/useriscart.do" );
            return null;
        }
        
	}

}
