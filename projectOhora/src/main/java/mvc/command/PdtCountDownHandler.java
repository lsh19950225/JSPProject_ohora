package mvc.command;

import java.sql.Connection; 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.ConnectionProvider;

import ohora.persistence.UserCartDAO;
import ohora.persistence.UserCartDAOImpl;

public class PdtCountDownHandler implements CommandHandler {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		System.out.println("pdtCountDownHandler test");

        int clistId = Integer.parseInt(request.getParameter("clist_id"));
        System.out.println("clistId : " + clistId);

        String contextPath = request.getContextPath();

        // try-with-resources를 사용하여 자동으로 Connection을 관리
        try (Connection conn = ConnectionProvider.getConnection()) {
            UserCartDAO dao = new UserCartDAOImpl(conn);

            boolean isUpdate = dao.pdtCountDown(clistId);
            System.out.println("isUpdate : " + isUpdate);

            if (isUpdate) {
                // 수량 감소가 성공한 경우 장바구니 페이지로 리다이렉트
                response.sendRedirect(contextPath + "/cart/useriscart.do");
            } else {
                // 수량 감소 실패 시 오류 메시지 처리
                request.setAttribute("error", "수량 감소 실패: 이미 최소 수량입니다.");
                response.sendRedirect(contextPath + "/cart/useriscart.do");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "상품 수량 감소 처리 중 오류가 발생했습니다.");
            response.sendRedirect(contextPath + "/cart/useriscart.do");
        }

        return null;
		
	}

}
