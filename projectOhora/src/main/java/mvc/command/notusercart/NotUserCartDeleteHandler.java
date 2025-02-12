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

public class NotUserCartDeleteHandler implements CommandHandler {
	 @Override
	    public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		 System.out.println("NotUserCartDeleteHandler.process() 호출");
	        
	        int pdtId = Integer.parseInt(request.getParameter("pdt_id")); // pdt_id로 통일
	        String contextPath = request.getContextPath();
	        
	        // 쿠키에서 현재 수량을 찾아서 재고 반환에 사용
	        int quantityToReturn = findProductQuantityFromCookie(request, pdtId);
	        
	        if (quantityToReturn > 0) {
	            // 상품 삭제 및 재고 반환
	            try (Connection conn = ConnectionProvider.getConnection()) {
	                NotUserCartDAO dao = new NotUserCartDAOImpl(conn);
	                if (dao.deletePdt(pdtId, quantityToReturn)) {
	                    System.out.println("장바구니 상품 삭제 및 재고 반환 성공");
	                } else {
	                    System.out.println("재고 반환 실패");
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	        
	        // 쿠키에서 해당 상품 제거 후 업데이트
	        updateBasketCookie(request, response, pdtId);

	        // 장바구니 페이지로 리다이렉트
	        response.sendRedirect(contextPath + "/cart/nousercart.do");
	        return null;
	    }

	    private int findProductQuantityFromCookie(HttpServletRequest request, int pdtId) {
	        Cookie[] cookies = request.getCookies();
	        if (cookies == null) return 0;

	        for (Cookie cookie : cookies) {
	            if ("basketItems".equals(cookie.getName())) {
	                String value = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
	                value = value.substring(1, value.length() - 1); // [] 제거
	                String[] items = value.split("\\},\\{");

	                for (String item : items) {
	                    item = item.replaceAll("^\\{|\\}$", "");
	                    String[] pairs = item.split(",");
	                    int itemId = -1;
	                    int itemQuantity = -1;
	                    
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
	                    if (itemId == pdtId) {
	                        return itemQuantity; // 삭제할 수량 반환
	                    }
	                }
	            }
	        }
	        return 0;
	    }

	    private void updateBasketCookie(HttpServletRequest request, HttpServletResponse response, int pdtId) throws Exception {
	        Cookie[] cookies = request.getCookies();
	        if (cookies == null) return;

	        for (Cookie cookie : cookies) {
	            if ("basketItems".equals(cookie.getName())) {
	                String value = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
	                value = value.substring(1, value.length() - 1); // [] 제거
	                String[] items = value.split("\\},\\{");

	                StringBuilder newCookieValue = new StringBuilder("[");
	                boolean first = true;

	                for (String item : items) {
	                    item = item.replaceAll("^\\{|\\}$", "");
	                    String[] pairs = item.split(",");
	                    int itemId = -1;
	                    int itemQuantity = -1;

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

	                    if (itemId != pdtId) {
	                        if (!first) newCookieValue.append(",");
	                        newCookieValue.append("{\"id\":").append(itemId)
	                                      .append(",\"quantity\":").append(itemQuantity).append("}");
	                        first = false;
	                    }
	                }
	                newCookieValue.append("]");
	                
	                Cookie newCookie = new Cookie("basketItems", URLEncoder.encode(newCookieValue.toString(), StandardCharsets.UTF_8));
	                newCookie.setPath("/");
	                newCookie.setMaxAge(24 * 60 * 60);
	                response.addCookie(newCookie);
	                break;
	            }
	        }
	    }
}
