package mvc.command;

import java.sql.Connection; 
import java.util.ArrayList;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.util.ConnectionProvider;
import com.util.JsonConverter;  // JsonConverter 추가
import ohora.domain.ProductDTO;
import ohora.persistence.ProductDAO;
import ohora.persistence.ProductDAOImpl;

public class ExtraProductHandler implements CommandHandler {

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("ExtraProductHandler process...");

        // 상품 ID를 통해 cat_no 가져오기
        String productId = request.getParameter("product_id");
        Connection conn = null;
        ProductDAO dao = null;
        ArrayList<ProductDTO> additionalProducts = new ArrayList<>();

        try {
            conn = ConnectionProvider.getConnection();
            dao = new ProductDAOImpl(conn);

            if (productId != null && !productId.trim().isEmpty()) {
                int pdtId = Integer.parseInt(productId);
                ProductDTO product = dao.selectProductById(pdtId);
                int catId = product.getCat_id();

                // 카테고리 번호에 따라 추가 구성 상품 ID 설정
                ArrayList<Integer> additionalProductIds;
                switch (catId) {
                    case 1:
                        additionalProductIds = new ArrayList<>(Arrays.asList(170, 174, 169, 172));
                        break;
                    case 2:
                        additionalProductIds = new ArrayList<>(Arrays.asList(170, 169));
                        break;
                    case 3:
                        additionalProductIds = new ArrayList<>(Arrays.asList(173, 175, 169));
                        break;
                    default:
                        additionalProductIds = new ArrayList<>();
                        break;
                }//switch

                // 추가 구성 상품 목록 가져오기
                for (int additionalId : additionalProductIds) {
                    ProductDTO additionalProduct = dao.selectProductById(additionalId);
                    if (additionalProduct != null) {
                        additionalProducts.add(additionalProduct);
                    }// if
                }// for
                
                
            }// if

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) conn.close();
        }

        // 추가 구성 상품 목록을 JSON으로 변환하여 응답
        String jsonResponse = JsonConverter.convertProductsToJson(additionalProducts);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
        
        System.out.println(jsonResponse);

        return null;  // 뷰 반환이 필요 없으므로 null 반환
    }
    
    
}
