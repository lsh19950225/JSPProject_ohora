package mvc.command.notusercart;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.ConnectionProvider;

import mvc.command.CommandHandler;
import ohora.domain.ProductDTO;
import ohora.persistence.NotUserCartDAO;
import ohora.persistence.NotUserCartDAOImpl;

public class NotUserIsCartHandler implements CommandHandler {

	  private static final int FREE_SHIPPING_THRESHOLD = 50000;
	    private static final int SHIPPING_FEE = 3000;
	    private static final String COOKIE_NAME = "basketItems";
	    
	    @Override
	    public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
	        try (Connection conn = ConnectionProvider.getConnection()) {
	        	Map<Integer, Integer> pdtIdsAndQuantities = getPdtIdsAndQuantities(request.getCookies());
	            
	            // 장바구니가 비어있는 경우
	            if (pdtIdsAndQuantities.isEmpty()) {
	            	System.out.println("장바구니 비어있다");
	                setDefaultAttributes(request);
	                return "/ohora/notusercart.jsp";
	            }
	            
	            NotUserCartDAO dao = new NotUserCartDAOImpl(conn);
	            ArrayList<ProductDTO> products = dao.bringProducts(new ArrayList<>(pdtIdsAndQuantities.keySet()));
	            
	            
	            // 각 상품에 수량 설정 및 총 수량 계산
	            int totalQuantity = 0; // 전체 quantity의 합산을 저장
	            for (ProductDTO product : products) {
	                int quantity = pdtIdsAndQuantities.getOrDefault(product.getPdt_id(), 1);
	                product.setPdt_count(quantity);
	                totalQuantity += quantity;
	            }
	            
	            
	            // 각 상품에 수량 설정
	            for (ProductDTO product : products) {
	                int quantity = pdtIdsAndQuantities.getOrDefault(product.getPdt_id(), 1);
	                product.setPdt_count(quantity);
	            }
	            
	            // 금액 계산
	            int totalAmount = 0;      // 총 상품금액
	            int totalDiscount = 0;    // 총 할인금액
	            
	            for (ProductDTO product : products) {
	                totalAmount += product.getPdt_amount() * product.getPdt_count();
	                int discountAmount = product.getPdt_amount() * product.getPdt_discount_rate() / 100;
	                totalDiscount += discountAmount * product.getPdt_count();
	            }
	            
	            // 실제 상품 금액 (할인 적용)
	            int actualAmount = totalAmount - totalDiscount;
	            
	            // 배송비 계산
	            int shippingFee = actualAmount >= FREE_SHIPPING_THRESHOLD ? 0 : SHIPPING_FEE;
	            
	            // 무료배송까지 남은 금액
	            int remainingForFree = actualAmount >= FREE_SHIPPING_THRESHOLD ? 
	                                 0 : FREE_SHIPPING_THRESHOLD - actualAmount;
	            
	            // 최종 결제금액
	            int finalAmount = actualAmount + shippingFee;
	            
	            // request에 속성 설정
	            request.setAttribute("cartItems", products);
	            request.setAttribute("totalAmount", totalAmount);         // 총 상품금액
	            request.setAttribute("totalDiscount", totalDiscount);     // 총 할인금액
	            request.setAttribute("shippingFee", shippingFee);        // 배송비
	            request.setAttribute("remainingForFree", remainingForFree); // 무료배송까지 남은 금액
	            request.setAttribute("finalAmount", finalAmount);         // 최종 결제금액
	            request.setAttribute("rewardPoints", (int)(finalAmount * 0.01)); // 적립예정 포인트
	            request.setAttribute("totalQuantity", totalQuantity);  // 총 quantity 값 설정
	            
	            return "/ohora/notusercart.jsp";
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	            setDefaultAttributes(request);
	            return "/ohora/notusercart.jsp";
	        }
	    }

	    private void setDefaultAttributes(HttpServletRequest request) {
	        request.setAttribute("cartItems", new ArrayList<>());
	        request.setAttribute("totalAmount", 0);
	        request.setAttribute("totalDiscount", 0);
	        request.setAttribute("shippingFee", SHIPPING_FEE);  // 기본 배송비 설정
	        request.setAttribute("remainingForFree", FREE_SHIPPING_THRESHOLD); // 무료배송까지 필요한 금액
	        request.setAttribute("finalAmount", 0);
	        request.setAttribute("rewardPoints", 0);
	        request.setAttribute("totalQuantity", 0);  // 기본 총 quantity 값 설정
	    }
	    
	    private Map<Integer, Integer> getPdtIdsAndQuantities(Cookie[] cookies) {
	        Map<Integer, Integer> pdtIdsAndQuantities = new HashMap<>();
	        if (cookies == null) return pdtIdsAndQuantities;
	        
	        for (Cookie cookie : cookies) {
	            if ("basketItems".equals(cookie.getName())) {
	                try {
	                    String value = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
	                    System.out.println("디코딩된 쿠키 값: " + value);
	                    
	                    // [] 제거하고 객체들을 분리
	                    value = value.substring(1, value.length() - 1);
	                    String[] items = value.split("\\},\\{");
	                    
	                    for (String item : items) {
	                        // 첫 번째와 마지막 항목의 중괄호 처리
	                        item = item.replaceAll("^\\{|\\}$", "");
	                        
	                        // "id":1,"quantity":1 형태의 문자열 파싱
	                        String[] pairs = item.split(",");
	                        int id = -1;
	                        int quantity = -1;
	                        
	                        for (String pair : pairs) {
	                            String[] keyValue = pair.split(":");
	                            if (keyValue.length == 2) {
	                                String key = keyValue[0].replaceAll("\"", "").trim();
	                                String value2 = keyValue[1].replaceAll("\"", "").trim();
	                                
	                                if ("id".equals(key)) {
	                                    id = Integer.parseInt(value2);
	                                } else if ("quantity".equals(key)) {
	                                    quantity = Integer.parseInt(value2);
	                                }
	                            }
	                        }
	                        
	                        if (id > 0 && quantity > 0) {
	                            pdtIdsAndQuantities.put(id, quantity);
	                        }
	                    }
	                    
	                    System.out.println("아이디랑 수량 제대로 잡혀 있으면 여기 출력!!!  : " + pdtIdsAndQuantities);
	                    
	                } catch (Exception e) {
	                    System.out.println("쿠키 파싱 중 오류: " + e.getMessage());
	                    e.printStackTrace();
	                }
	                break;
	            }
	        }
	        return pdtIdsAndQuantities;
	    }
}