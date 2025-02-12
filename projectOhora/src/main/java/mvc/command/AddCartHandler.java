package mvc.command;

import java.io.PrintWriter; 
import java.sql.Connection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.ConnectionProvider;

import net.sf.json.JSONObject;
import ohora.persistence.UserCartDAO;
import ohora.persistence.UserCartDAOImpl;

public class AddCartHandler implements CommandHandler {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		System.out.println("addCartHandler test");
        
        int userId = Integer.parseInt(request.getParameter("user_id"));
        int pdtId = Integer.parseInt(request.getParameter("pdt_id"));
        
        System.out.println("userId : " + userId);
        System.out.println("pdtId : " + pdtId);
        
        // JSON 응답 준비
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        JSONObject jsonResponse = new JSONObject();

        try (Connection conn = ConnectionProvider.getConnection()) {
            UserCartDAO dao = new UserCartDAOImpl(conn);
            Map<String, Object> cartResult = dao.addCart(userId, pdtId, request);
            
            // Map에서 productCount와 message를 가져옵니다.
            int productCount = (int) cartResult.get("productCount");
            String message = (String) cartResult.get("message");
            
            System.out.println("productCount : " + productCount);
            System.out.println("message : " + message);
            
            // JSON 응답에 productCount와 message 데이터 추가
            jsonResponse.put("newCartCount", productCount);
            jsonResponse.put("message", message);
            
        } catch (Exception e) {
            e.printStackTrace();
            // 오류 발생 시 JSON 응답에 오류 메시지 추가
            jsonResponse.put("error", "An error occurred while adding to the cart.");
        }

        // JSON 응답 전송
        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }

        // JSON 응답을 보냈으므로 null 반환
        return null;
        
	}
	
}
