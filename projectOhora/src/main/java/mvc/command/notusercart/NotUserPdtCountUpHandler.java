package mvc.command.notusercart;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.ConnectionProvider;

import mvc.command.CommandHandler;
import ohora.persistence.NotUserCartDAO;
import ohora.persistence.NotUserCartDAOImpl;

public class NotUserPdtCountUpHandler implements CommandHandler{
	   @Override
	   public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
	      
	      System.out.println("pdtCountUpHandler test");

	        // 클라이언트에서 전달된 파라미터를 받는다
	      	int pdtId = Integer.parseInt(request.getParameter("pdt_id"));
	        int quantity = Integer.parseInt(request.getParameter("pdt_count"));
	        
	        System.out.println("pdt_id 받아온 건? ->" + pdtId);
	        System.out.println("pdt_count 받아온 건 ? -> " + quantity);
	        
	        String contextPath = request.getContextPath();

	      
	        
	        try (Connection conn = ConnectionProvider.getConnection()) {
	            NotUserCartDAO dao = new NotUserCartDAOImpl(conn);
	            
	            // 상품 수량 증가 처리
	            boolean isUpdated = dao.noPdtCountUp(pdtId, quantity);
	            
	            if (isUpdated) {
	                // 수량 증가가 성공적으로 처리되었을 때
	            	
	            	 Cookie[] cookies = request.getCookies();
	                 if (cookies != null) {
	                     for (Cookie cookie : cookies) {
	                         if ("basketItems".equals(cookie.getName())) {
	                             String value = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
	                             // [] 제거
	                             value = value.substring(1, value.length() - 1);
	                             String[] items = value.split("\\},\\{");
	                             
	                             StringBuilder newCookieValue = new StringBuilder("[");
	                             boolean first = true;
	                             
	                             for (String item : items) {
	                                 item = item.replaceAll("^\\{|\\}$", "");
	                                 String[] pairs = item.split(",");
	                                 int itemId = -1;
	                                 int itemQuantity = -1;
	                                 
	                                 // id와 quantity 파싱
	                                 for (String pair : pairs) {
	                                     String[] keyValue = pair.split(":");
	                                     if (keyValue.length == 2) {
	                                         String key = keyValue[0].replaceAll("\"", "").trim();
	                                         String val = keyValue[1].replaceAll("\"", "").trim();
	                                         
	                                         if ("id".equals(key)) {
	                                             itemId = Integer.parseInt(val);
	                                         } else if ("quantity".equals(key)) {
	                                             itemQuantity = Integer.parseInt(val);
	                                         }
	                                     }
	                                 }
	                                 
	                                 // 해당 상품의 수량 증가
	                                 if (itemId == pdtId) {
	                                     itemQuantity++;
	                                 }
	                                 
	                                 // 새 쿠키값에 추가
	                                 if (!first) {
	                                     newCookieValue.append(",");
	                                 }
	                                 newCookieValue.append("{\"id\":").append(itemId)
	                                              .append(",\"quantity\":").append(itemQuantity).append("}");
	                                 first = false;
	                             }
	                             
	                             newCookieValue.append("]");
	                             
	                             // 새 쿠키 설정
	                             Cookie newCookie = new Cookie("basketItems", 
	                                 URLEncoder.encode(newCookieValue.toString(), StandardCharsets.UTF_8));
	                             newCookie.setPath("/");
	                             newCookie.setMaxAge(24 * 60 * 60); // 24시간
	                             response.addCookie(newCookie);
	                             break;
	                         }
	                     }
	                 }
	                 
	                 	
	                System.out.println("장바구니 상품 수량 증가 성공");
	                response.sendRedirect( contextPath + "/cart/nousercart.do" );
	                return null;
	            } else {
	                // 재고 부족 또는 다른 이유로 실패한 경우
	                System.out.println("수량 증가 실패: 재고 부족 또는 오류");
	                request.setAttribute("error", "재고보다 많은 수량을 요청했습니다.");
	                response.sendRedirect( contextPath + "/cart/nousercart.do" );
	                return null;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            request.setAttribute("error", "상품 수량 증가 처리 중 오류가 발생했습니다.");
	            response.sendRedirect( contextPath + "/cart/nousercart.do" );
	            return null;
	        }
	        
	   }

}
